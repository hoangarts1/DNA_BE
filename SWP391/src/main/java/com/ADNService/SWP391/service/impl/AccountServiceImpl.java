package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.AccountDTO;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.repository.AccountRepository;
import com.ADNService.SWP391.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));
    }

    @Override
    public Account updateAccountInfo(Long id, AccountDTO dto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));

        account.setEmail(dto.getEmail());
        account.setPhone(dto.getPhone());
        account.setFullName(dto.getFullName());

        return accountRepository.save(account);
    }

    @Override
    public void deactivateAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));
        account.setActive(false);
        accountRepository.save(account);
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with username: " + username));
    }

    @Override
    public void activateAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));
        account.setActive(true);
        accountRepository.save(account);
    }
}
