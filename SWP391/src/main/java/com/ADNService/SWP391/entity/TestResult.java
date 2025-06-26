package com.ADNService.SWP391.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_result")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private TestOrder testOrder;

    @ManyToOne
    @JoinColumn(name = "sample_id1")
    private TestSample sampleId1; // Thêm trường sampleId1

    @ManyToOne
    @JoinColumn(name = "sample_id2")
    private TestSample sampleId2; // Thêm trường sampleId2

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String result;

    @Column(unique = true, columnDefinition = "NVARCHAR(255)")
    private String resultPercent;


    // Constructors
    public TestResult() {}

    public TestResult(Long id, TestOrder testOrder, TestSample sampleId1, TestSample sampleId2, String result, String resultPercent) {
        this.id = id;
        this.testOrder = testOrder;
        this.sampleId1 = sampleId1;
        this.sampleId2 = sampleId2;
        this.result = result;
        this.resultPercent = resultPercent;
    }

    // Getters and setters
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

    public TestSample getSampleId1() {
        return sampleId1;
    }

    public void setSampleId1(TestSample sampleId1) {
        this.sampleId1 = sampleId1;
    }

    public TestSample getSampleId2() {
        return sampleId2;
    }

    public void setSampleId2(TestSample sampleId2) {
        this.sampleId2 = sampleId2;
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

}