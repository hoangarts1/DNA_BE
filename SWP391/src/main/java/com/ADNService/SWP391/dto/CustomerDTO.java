package com.ADNService.SWP391.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;
    private Long accountId;
    private String address;
    private String gender;
    private Date dateOfBirth;
    private String documentType;
    private String placeOfIssue;
    private Date dateOfIssue;
    private String fingerprint;

    public CustomerDTO(Long accountId, String address, Date dateOfBirth, Date dateOfIssue, String documentType, String fingerprint, String gender, Long id, String placeOfIssue) {
        this.accountId = accountId;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.dateOfIssue = dateOfIssue;
        this.documentType = documentType;
        this.fingerprint = fingerprint;
        this.gender = gender;
        this.id = id;
        this.placeOfIssue = placeOfIssue;
    }

    public CustomerDTO() {
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
