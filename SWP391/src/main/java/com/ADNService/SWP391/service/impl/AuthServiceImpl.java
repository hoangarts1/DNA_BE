package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.enums.Role;
import com.ADNService.SWP391.exception.CustomException;
import com.ADNService.SWP391.repository.AccountRepository;
import com.ADNService.SWP391.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AccountRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Account register(AccountDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new CustomException("Username already exists.");
        }else if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new CustomException("Email already exists.");
        }else if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new CustomException("Email already exists.");
        }else if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new CustomException("Email already exists.");
        }else if (userRepository.existsByPhone(userDTO.getPhone())) {
            throw new CustomException("Phone already exists.");
        }

        Account user = new Account();

        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setFullName(userDTO.getFullName());
        user.setAddress(userDTO.getAddress());
        user.setRole(Role.CUSTOMER);
        user.setActive(true);

        return userRepository.save(user);
    }

    @Override
    public Account login(String username, String password) {
        Account user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException("Invalid username or password");
        }

        return user;
    }
}
