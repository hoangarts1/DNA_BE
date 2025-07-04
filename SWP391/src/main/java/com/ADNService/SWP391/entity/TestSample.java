package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TestSample")
public class TestSample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship
    @OneToMany(mappedBy = "testSample", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResultSample> testResultSamples;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private TestOrder order;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String gender;

    private Date dateOfBirth;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String documentType;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String documentNumber;

    private Date dateOfIssue;

    private Date expirationDate;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String placeOfIssue;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String nationality;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String address;

    @ManyToOne
    @JoinColumn(name = "sample_type_id", nullable = false)
    private SampleType sampleType;


    private Integer numberOfSample;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String relationship;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String medicalHistory;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String fingerprint;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String kitCode;

    public String getKitCode() {
        return kitCode;
    }

    public void setKitCode(String kitCode) {
        this.kitCode = kitCode;
    }


    public TestSample() {
    }

    public TestSample(Long id, List<TestResultSample> testResultSamples, TestOrder order, Customer customer, String name, String gender, Date dateOfBirth, String documentType, String documentNumber, Date dateOfIssue, Date expirationDate, String placeOfIssue, String nationality, String address, SampleType sampleType, Integer numberOfSample, String relationship, String medicalHistory, String fingerprint, String kitCode) {
        this.id = id;
        this.testResultSamples = testResultSamples;
        this.order = order;
        this.customer = customer;
        this.name = name;
        this.gender = gender;
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
        this.kitCode = kitCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestOrder getOrder() {
        return order;
    }

    public void setOrder(TestOrder order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public List<TestResultSample> getTestResultSamples() {
        return testResultSamples;
    }

    public void setTestResultSamples(List<TestResultSample> testResultSamples) {
        this.testResultSamples = testResultSamples;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
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
