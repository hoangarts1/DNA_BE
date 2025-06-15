package com.ADNService.SWP391.dto;

import com.ADNService.SWP391.enums.Role;
import com.ADNService.SWP391.enums.StaffType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private StaffType staffType; // enum: NORMAL_STAFF, LAB_STAFF



    public AccountDTO() {
    }

    public AccountDTO(String username, String password, String fullName, String email, String phone, Role role, StaffType staffType) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.staffType = staffType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public StaffType getStaffType() {return staffType;}
    public void setStaffType(StaffType staffType) {
        this.staffType = staffType;
    };
}
