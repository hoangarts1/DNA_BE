package com.ADNService.SWP391.entity;

import com.ADNService.SWP391.enums.Role;
import com.ADNService.SWP391.enums.StaffType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long
            id;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Column(unique = true, columnDefinition = "NVARCHAR(MAX)")
    private String fingerprint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffType role;

    public Staff() {
    }

    public Staff(Account account, String fingerprint, Long id, StaffType role) {
        this.account = account;
        this.fingerprint = fingerprint;
        this.id = id;
        this.role = role;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public StaffType getRole() {
        return role;
    }

    public void setRole(StaffType role) {
        this.role = role;
    }
}
