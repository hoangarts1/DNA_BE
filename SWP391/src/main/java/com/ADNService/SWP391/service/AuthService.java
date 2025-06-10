package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.UserDTO;
import com.ADNService.SWP391.entity.Account;

public interface AuthService {
    Account register(UserDTO userDTO);
    Account login(String username, String password);
}
