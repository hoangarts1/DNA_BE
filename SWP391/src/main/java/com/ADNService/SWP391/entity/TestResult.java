package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "TestResult")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private TestOrder testOrder;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String result;

    private String resultPercent;

    private String resultUrl;

    public TestResult() {
    }

    public TestResult(Long id, TestOrder testOrder, Customer customer, String result, String resultPercent, String resultUrl) {
        this.id = id;
        this.testOrder = testOrder;
        this.customer = customer;
        this.result = result;
        this.resultPercent = resultPercent;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultPercent() {
        return resultPercent;
    }

    public void setResultPercent(String resultPercent) {
        this.resultPercent = resultPercent;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }
}
