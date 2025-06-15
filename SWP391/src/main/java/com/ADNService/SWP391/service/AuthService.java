package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.dto.LoginResponse;
import com.ADNService.SWP391.entity.Account;

public interface AuthService {
    Account register(AccountDTO userDTO);
//    Account login(String username, String password);
    LoginResponse login(String username, String password);

    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
    Account createStaff(AccountDTO userDTO);

}
