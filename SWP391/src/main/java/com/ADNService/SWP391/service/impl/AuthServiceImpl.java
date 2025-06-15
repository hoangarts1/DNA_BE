package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.config.JwtUtil;
import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.dto.LoginResponse;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.enums.Role;
import com.ADNService.SWP391.exception.CustomException;
import com.ADNService.SWP391.repository.AccountRepository;
import com.ADNService.SWP391.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AccountRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender mailSender;

    private final long resetTokenExpiryMillis = 2 * 60 * 1000;

    @Override
    public Account register(AccountDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new CustomException("Username already exists.");
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
        user.setRole(userDTO.getRole());
        user.setActive(true);

        return userRepository.save(user);
    }


    @Override
    public LoginResponse login(String username, String password) {
        Account user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException("Invalid username or password");
        }

//        String token = jwtUtil.generateToken(username, user.getRole().name());
        String token = jwtUtil.generateToken(username, user.getRole().name());

        return new LoginResponse(token, user);
    }

    @Override
    public void forgotPassword(String email) {
            Account user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException("Email not found"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

            String resetLink = "http://localhost:3000/reset-password?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Reset your password");
            message.setText("Click the following link to reset your password: " + resetLink);

            mailSender.send(message);


//        Optional<Account> optionalUser = userRepository.findByEmail(email);
//        if (!optionalUser.isPresent()) {
//            throw new CustomException("Email not found");
//        }
//
//        Account user = optionalUser.get();
//
//        String token = jwtUtil.generateToken(user.getUsername());
//
//        System.out.println("Reset link: http://localhost:8080/auth/reset-password?token=" + token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        try {
            String username = jwtUtil.extractUsername(token);
            Account user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomException("Tài khoản không tồn tại."));
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } catch (ExpiredJwtException e) {
            throw new CustomException("Token đã hết hạn.");
        } catch (JwtException e) {
            throw new CustomException("Token không hợp lệ.");
        }
    }

    @Override
    public Account createStaff(AccountDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new CustomException("Username already exists.");
        } else if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new CustomException("Email already exists.");
        } else if (userRepository.existsByPhone(userDTO.getPhone())) {
            throw new CustomException("Phone already exists.");
        }

        Account staff = new Account();
        staff.setUsername(userDTO.getUsername());
        staff.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        staff.setEmail(userDTO.getEmail());
        staff.setPhone(userDTO.getPhone());
        staff.setFullName(userDTO.getFullName());
        staff.setRole(userDTO.getRole());
        staff.setActive(true);

        return userRepository.save(staff);
    }



}



