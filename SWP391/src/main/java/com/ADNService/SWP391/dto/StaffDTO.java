package com.ADNService.SWP391.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StaffDTO {
    private Long id;
    private Long accountId;
    private String fingerprint;
    private String role;

    public StaffDTO() {
    }

    public StaffDTO(Long accountId, String fingerprint, Long id, String role) {
        this.accountId = accountId;
        this.fingerprint = fingerprint;
        this.id = id;
        this.role = role;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
