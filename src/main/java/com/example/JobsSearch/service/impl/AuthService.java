package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.model.User;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

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

    public ResponseObject seekerSignup(SeekerSignupRequest seekerSignupRequest) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(seekerSignupRequest.getUsername())) {
            return ResponseObject.message("Username already taken");
        }
        // Tạo đối tượng User từ thông tin đăng ký
        User user = new User(seekerSignupRequest.getUsername(), passwordEncoder.encode(seekerSignupRequest.getPassword())
                , "ROLE_SEEKER");

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
        User user = new User();
        user.setUsername(hrSignupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(hrSignupRequest.getPassword()));
        user.setRole("ROLE_HR");
        userRepository.save(user);
        HiringOrganization hiringOrganization = new HiringOrganization(user, hrSignupRequest.getName()
                , hrSignupRequest.getPhone_number(), hrSignupRequest.getWebsite()
                , hrSignupRequest.getAddress(), hrSignupRequest.getIntroduction(), hrSignupRequest.getOrganizationType());
        hiringOrganizationRepository.save(hiringOrganization);

        return ResponseObject.status(true).setData(hiringOrganization);
    }


}