package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Account register(@RequestBody AccountDTO userDTO) {
        return authService.register(userDTO);
    }

    @PostMapping("/login")
    public Account login(@RequestBody AccountDTO loginDTO) {
        return authService.login(loginDTO.getUsername(), loginDTO.getPassword());
    }
}