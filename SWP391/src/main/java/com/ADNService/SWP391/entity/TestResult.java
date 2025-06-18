package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "TestResult")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private TestOrder testOrder;

    @ManyToOne
    @JoinColumn(name = "test_result_sample_id")
    private TestResultSample testResultSample;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String result;

    private String resultUrl;

    public TestResult() {
    }

    public TestResult(Long id, TestOrder testOrder, TestResultSample testResultSample, Account account, String result, String resultUrl) {
        this.id = id;
        this.testOrder = testOrder;
        this.testResultSample = testResultSample;
        this.account = account;
        this.result = result;
        this.resultUrl = resultUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestOrder getTestOrder() {
        return testOrder;
    }

    public void setTestOrder(TestOrder testOrder) {
        this.testOrder = testOrder;
    }

    public TestResultSample getTestResultSample() {
        return testResultSample;
    }

    public void setTestResultSample(TestResultSample testResultSample) {
        this.testResultSample = testResultSample;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }
}
