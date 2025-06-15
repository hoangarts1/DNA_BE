package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.config.JwtUtil;
import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.dto.ForgotPasswordRequest;
import com.ADNService.SWP391.dto.LoginResponse;
import com.ADNService.SWP391.dto.ResetPasswordRequest;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.exception.CustomException;
import com.ADNService.SWP391.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody AccountDTO loginDTO) {
        return authService.login(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @PostMapping("/register")
    public Account register(@RequestBody AccountDTO userDTO) {
        return authService.register(userDTO);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return "Reset password email sent.";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("newPassword") String newPassword) {
        authService.resetPassword(token, newPassword);
        return "doi mk thanh cong";
    }

    @PostMapping("/create-staff")
    public ResponseEntity<Account> createStaff(@RequestBody AccountDTO staffDTO) {
        Account newStaff = authService.createStaff(staffDTO);
        return ResponseEntity.ok(newStaff);
    }
}