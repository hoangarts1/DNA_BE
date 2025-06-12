package com.ADNService.SWP391.dto;

import java.util.Date;


public class TestSampleDTO {
    private Long id;
    private Long orderId;
    private Long customerId;
    private Long staffId;

    private String name;
    private Date dateOfBirth;
    private String documentType;
    private String documentNumber;
    private Date dateOfIssue;
    private Date expirationDate;
    private String placeOfIssue;
    private String nationality;
    private String address;
    private String sampleType;
    private Integer numberOfSample;
    private String relationship;
    private String medicalHistory;
    private String fingerprint;

    public TestSampleDTO() {
    }

    public TestSampleDTO(Long id, Long orderId, Long customerId, Long staffId, String name, Date dateOfBirth, String documentType, String documentNumber, Date dateOfIssue, Date expirationDate, String placeOfIssue, String nationality, String address, String sampleType, Integer numberOfSample, String relationship, String medicalHistory, String fingerprint) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.staffId = staffId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.dateOfIssue = dateOfIssue;
        this.expirationDate = expirationDate;
        this.placeOfIssue = placeOfIssue;
        this.nationality = nationality;
        this.address = address;
        this.sampleType = sampleType;
        this.numberOfSample = numberOfSample;
        this.relationship = relationship;
        this.medicalHistory = medicalHistory;
        this.fingerprint = fingerprint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public Integer getNumberOfSample() {
        return numberOfSample;
    }

    public void setNumberOfSample(Integer numberOfSample) {
        this.numberOfSample = numberOfSample;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
