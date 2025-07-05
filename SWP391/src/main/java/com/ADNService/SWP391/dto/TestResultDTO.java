package com.ADNService.SWP391.dto;

public class TestResultDTO {
    private Long id;
    private Long orderId;
    private Long sampleId1;
    private Long sampleId2;
    private String result;
    private String resultPercent;

    public TestResultDTO() {
    }

    public TestResultDTO(Long id, Long orderId, Long sampleId1, Long sampleId2, String result, String resultPercent) {
        this.id = id;
        this.orderId = orderId;
        this.sampleId1 = sampleId1;
        this.sampleId2 = sampleId2;
        this.result = result;
        this.resultPercent = resultPercent;
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
