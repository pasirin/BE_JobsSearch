package com.example.JobsSearch.controller;

import com.example.JobsSearch.payload.Request.HrSignupRequest;
import com.example.JobsSearch.payload.Request.LoginRequest;
import com.example.JobsSearch.payload.Request.SeekerSignupRequest;
import com.example.JobsSearch.payload.Request.UtilRequest.ChangePasswordRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.security.UserDetailsImpl;
import com.example.JobsSearch.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok().body(authService.login(loginRequest));
  }

  @PostMapping("/seeker/signup")
  public ResponseEntity<?> register(@Valid @RequestBody SeekerSignupRequest seekerSignupRequest) {
    ResponseObject output = authService.seekerSignup(seekerSignupRequest);
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PostMapping("/organization/signup")
  public ResponseEntity<?> hrRegister(@Valid @RequestBody HrSignupRequest hrSignupRequest) {
    ResponseObject output = authService.hrSignup(hrSignupRequest);
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    // Lấy thông tin authentication của người dùng hiện tại
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      // Xóa thông tin authentication và phiên làm việc hiện tại
      new SecurityContextLogoutHandler().logout(request, null, authentication);
    }

    return ResponseEntity.ok("Đăng xuất thành công");
  }

  @PostMapping("/change-password")
  public ResponseEntity<?> changePassword(
      @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
    UserDetailsImpl object;
    try {
      object =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
    }
    ResponseObject output =
        authService.changePassword(object.getId(), object.getUsername(), changePasswordRequest);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PutMapping("/users")
  public ResponseEntity<?> changeEmail(@RequestBody String email) {
    UserDetailsImpl object;
    try {
      object =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
    }
    ResponseObject output = authService.changeEmail(object.getId(), email);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@Valid @RequestParam String email) {
    ResponseObject output = authService.updateResetPasswordToken(email);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(
      @Valid @RequestParam String token, @RequestParam String newPassword) {
    ResponseObject output = authService.resetPassword(token, newPassword);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }
}
