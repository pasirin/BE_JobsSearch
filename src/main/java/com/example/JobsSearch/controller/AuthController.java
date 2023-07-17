package com.example.JobsSearch.controller;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.model.User;
import com.example.JobsSearch.payload.Request.HrSignupRequest;
import com.example.JobsSearch.payload.Request.LoginRequest;
import com.example.JobsSearch.payload.Request.SeekerSignupRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    @PostMapping("/seeker/signup")
    public ResponseEntity<?> register(@RequestBody SeekerSignupRequest seekerSignupRequest) {
        ResponseObject output = authService.seekerSignup(seekerSignupRequest);
        return output.getStatus() ? ResponseEntity.ok(output.getData()) : ResponseEntity.badRequest().body(output.getMessage());

    }

    @PostMapping("/organization/signup")
    public ResponseEntity<?> hrRegister(@RequestBody HrSignupRequest hrSignupRequest) {
        ResponseObject output = authService.hrSignup(hrSignupRequest);
        return output.getStatus() ? ResponseEntity.ok(output.getData()) : ResponseEntity.badRequest().body(output.getMessage());
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Lấy thông tin authentication của người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Xóa thông tin authentication và phiên làm việc hiện tại
            new SecurityContextLogoutHandler().logout(request, null, authentication);
        }

        return ResponseEntity.ok("Đăng xuất thành công");
    }
}
