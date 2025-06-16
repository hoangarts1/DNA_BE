package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.entity.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();
    Account getAccountById(Long id);
    Account updateAccountInfo(Long id, AccountDTO dto);
    void deactivateAccount(Long id);
    Account findByUsername(String username);
}
