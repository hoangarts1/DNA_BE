package com.ADNService.SWP391.dto;

public class TestResultDTO {
    private Long id;
    private Long orderId;
    private Long customerId;
    private String result;
    private String resultPercent;
    private String resultUrl;

    public TestResultDTO() {
    }

    public TestResultDTO(Long id, Long orderId, Long customerId, String result, String resultPercent, String resultUrl) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
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
