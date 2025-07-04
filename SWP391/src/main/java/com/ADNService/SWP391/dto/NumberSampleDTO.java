package com.ADNService.SWP391.dto;

public class NumberSampleDTO {
    private Long id;
    private int baseQuantity;
    private double extraPricePerSample;
    private String serviceType;

    public NumberSampleDTO() {}

    public NumberSampleDTO(Long id, int baseQuantity, double extraPricePerSample, String serviceType) {
        this.id = id;
        this.baseQuantity = baseQuantity;
        this.extraPricePerSample = extraPricePerSample;
        this.serviceType = serviceType;
    }

    public Long getId() {
        return id;
    }

    public int getBaseQuantity() {
        return baseQuantity;
    }

    public double getExtraPricePerSample() {
        return extraPricePerSample;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBaseQuantity(int baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public void setExtraPricePerSample(double extraPricePerSample) {
        this.extraPricePerSample = extraPricePerSample;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
