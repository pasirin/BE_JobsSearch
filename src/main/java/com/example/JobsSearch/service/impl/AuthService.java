package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.model.User;
import com.example.JobsSearch.model.util.ERole;
import com.example.JobsSearch.model.util.EStatus;
import com.example.JobsSearch.payload.Request.HrSignupRequest;
import com.example.JobsSearch.payload.Request.LoginRequest;
import com.example.JobsSearch.payload.Request.SeekerSignupRequest;
import com.example.JobsSearch.payload.Response.JwtResponse;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.HiringOrganizationRepository;
import com.example.JobsSearch.repository.SeekerRepository;
import com.example.JobsSearch.repository.UserRepository;
import com.example.JobsSearch.security.JwtTokenProvider;
import com.example.JobsSearch.security.UserDetailsImpl;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SeekerRepository seekerRepository;
    @Autowired
    private HiringOrganizationRepository hiringOrganizationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    AuthenticationManager authenticationManager;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @EventListener
    public void initAdmin(ApplicationReadyEvent event) {
        if (!userRepository.existsByUsername(adminUsername)) {
            User user = new User(adminUsername, passwordEncoder.encode(adminPassword), ERole.ROLE_ADMIN);
            user.setStatus(EStatus.ACTIVE);
            userRepository.save(user);
        }
    }

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles =
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
    }

    public static boolean isPasswordValid(String password) {
        String PASSWORD_PATTERN =
                "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z0-9!@#$%^&*(),.?\":{}|<>]{8,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    public ResponseObject seekerSignup(SeekerSignupRequest seekerSignupRequest) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(seekerSignupRequest.getUsername())) {
            return ResponseObject.message("Username already taken");
        }
        if (isPasswordValid(seekerSignupRequest.getPassword())) {
            return ResponseObject.message("Mật khẩu phải tuân thủ nguyên tắc có ít nhất 8 ký tự, chứa ít nhất 1 ký tự viết hoa, 1 ký tự số, 1 ký tự đặc biệt");
        }
        // Tạo đối tượng User từ thông tin đăng ký
        User user =
                new User(
                        seekerSignupRequest.getUsername(),
                        passwordEncoder.encode(seekerSignupRequest.getPassword()),
                        ERole.ROLE_SEEKER);
        user.setStatus(EStatus.ACTIVE);
        // Lưu user vào cơ sở dữ liệu
        userRepository.save(user);
        seekerRepository.save(new Seeker(user));
        return ResponseObject.status(true).setData(user);
    }

    public ResponseObject hrSignup(HrSignupRequest hrSignupRequest) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(hrSignupRequest.getUsername())) {
            return ResponseObject.message("Username Already Taken");
        }
        // Kiểm tra tên công ty đã tồn tại chưa (một công ty chỉ được đăng ký 1 tài khoản)
        if (hiringOrganizationRepository.existsByName(hrSignupRequest.getName())) {
            return ResponseObject.message("Company Name Already Taken");
        }
        if (isPasswordValid(hrSignupRequest.getPassword())) {
            return ResponseObject.message("Mật khẩu phải tuân thủ nguyên tắc có ít nhất 8 ký tự, chứa ít nhất 1 ký tự viết hoa, 1 ký tự số, 1 ký tự đặc biệt");
        }
        if (userRepository.findByEmail(hrSignupRequest.getEmail()).isPresent()) {
            return ResponseObject.message("Email đã tồn tại trong cơ sở dữ liệu.");
        }
        User user = new User();
        user.setUsername(hrSignupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(hrSignupRequest.getPassword()));
        user.setEmail(hrSignupRequest.getEmail());
        user.setRole(ERole.ROLE_HR);
        user.setStatus(EStatus.ACTIVE);
        userRepository.save(user);

        HiringOrganization hiringOrganization = new HiringOrganization();
        hiringOrganization.setUser(user);
        hiringOrganization.setName(hrSignupRequest.getName());
        hiringOrganization.setPhoneNumber(hrSignupRequest.getPhone_number());
        hiringOrganization.setWebsite(hrSignupRequest.getWebsite());
        hiringOrganization.setAddress(hrSignupRequest.getAddress());
        hiringOrganization.setIntroduction(hrSignupRequest.getIntroduction());

        hiringOrganizationRepository.save(hiringOrganization);
        return ResponseObject.status(true).setData(hiringOrganization);
    }

    public ResponseObject changePassword(Long id, String username, String password) {
        if (userRepository.findById(id).isEmpty()) {
            return ResponseObject.message("There Aren't any user with the requested Id");
        }
        User user = userRepository.findById(id).get();
        if (!user.getUsername().equals(username)) {
            return ResponseObject.message("ERROR: Conflicting username and id");
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return ResponseObject.ok();
    }

    public ResponseObject changeEmail(Long id, String Email) {
        if (userRepository.findById(id).isEmpty()) {
            return ResponseObject.message("There Aren't any user with the requested Id");
        }
        User user = userRepository.findById(id).get();
        user.setEmail(Email);
        userRepository.save(user);
        return ResponseObject.ok();
    }

    public ResponseObject updateResetPasswordToken(String email) {
        String token = RandomString.make(30);
        if (userRepository.findByEmail(email).isEmpty()) {
            return ResponseObject.message("There Aren't any user with the requested Email");
        }
        User user = userRepository.findByEmail(email).get();
        user.setResetPasswordToken(token);
        userRepository.save(user);
        try {
            sendEmail(email, token);
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            return ResponseObject.message(e.toString());
        }
        return ResponseObject.ok();
    }

    public ResponseObject resetPassword(String token, String newPassword) {
        if (userRepository.findByResetPasswordToken(token).isEmpty()) {
            return ResponseObject.message("There Aren't any user with the requested Token");
        }
        User user = userRepository.findByResetPasswordToken(token).get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
        return ResponseObject.ok();
    }

    private void sendEmail(String email, String token)
            throws MessagingException, UnsupportedEncodingException {
        JavaMailSender mailSender = getJavaMailSender();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("jobSeekPro@searchMe.com", "Job Seeker Support");
        helper.setTo(email);

        String subject = "Here's the token to reset your password";

        String content =
                "<p>Hello,</p>"
                        + "<p>You have requested to reset your password.</p>"
                        + "<p>Below is the token to reset your password:</p>"
                        + "<p>"
                        + token
                        + "</p>"
                        + "<br>"
                        + "<p>Ignore this email if you do remember your password, "
                        + "or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender1 = new JavaMailSenderImpl();
        mailSender1.setHost("smtp.gmail.com");
        mailSender1.setPort(587);

        mailSender1.setUsername("duongdungnhan1@gmail.com");
        mailSender1.setPassword("fctiybxphpkroftf");

        Properties props = mailSender1.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender1;
    }
}
