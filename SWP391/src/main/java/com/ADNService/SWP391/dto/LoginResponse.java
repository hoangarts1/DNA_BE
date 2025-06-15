package com.ADNService.SWP391.dto;

import com.ADNService.SWP391.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

public class LoginResponse {
    private String token;
    private Account account;

    public LoginResponse() {
    }

    public LoginResponse(String token, Account account) {
        this.token = token;
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
