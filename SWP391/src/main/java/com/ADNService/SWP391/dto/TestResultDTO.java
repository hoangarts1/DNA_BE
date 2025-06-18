package com.ADNService.SWP391.dto;

public class TestResultDTO {
    private Long id;
    private Long orderId;
    private Long testResultSampleId;
    private Long accountId;
    private String result;
    private String resultUrl;
    private Long sampleId1;
    private Long sampleId2;


    public TestResultDTO() {
    }

    public TestResultDTO(Long id, Long orderId, Long testResultSampleId, Long accountId, String result, String resultUrl) {
        this.id = id;
        this.orderId = orderId;
        this.testResultSampleId = testResultSampleId;
        this.accountId = accountId;
        this.result = result;
        this.resultUrl = resultUrl;
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

    public Long getTestResultSampleId() {
        return testResultSampleId;
    }

    public void setTestResultSampleId(Long testResultSampleId) {
        this.testResultSampleId = testResultSampleId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public Long getSampleId1() {
        return sampleId1;
    }

    public void setSampleId1(Long sampleId1) {
        this.sampleId1 = sampleId1;
    }

    public Long getSampleId2() {
        return sampleId2;
    }

    public void setSampleId2(Long sampleId2) {
        this.sampleId2 = sampleId2;
    }
}
