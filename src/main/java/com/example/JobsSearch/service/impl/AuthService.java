package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.User;
import com.example.JobsSearch.payload.Request.LoginRequest;
import com.example.JobsSearch.payload.Request.SignupRequest;
import com.example.JobsSearch.payload.Response.JwtResponse;
import com.example.JobsSearch.repository.UserRepository;
import com.example.JobsSearch.security.jwt.JwtTokenProvider;
import com.example.JobsSearch.security.jwt.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenUtil;
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

    public User signup(SignupRequest registrationRequest) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        // Tạo đối tượng User từ thông tin đăng ký
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        String role = registrationRequest.getRole();
        if (role == null) {
            user.setRole("ROLE_SEEKER");
        } else {
            switch (role) {
                case "admin":
                    user.setRole("ROLE_ADMIN");
                    break;
                case "hr":
                    user.setRole("ROLE_HR");
                    break;
                case "seeker":
                    user.setRole("ROLE_SEEKER");
                    break;
                default:
                    user.setRole("ROLE_USER");
                    break;
            }
        }
        // Lưu user vào cơ sở dữ liệu
        return userRepository.save(user);
    }
}