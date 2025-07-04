package com.ADNService.SWP391.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "number_sample")
public class NumberSample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "number_sample_id")
    private Long id;

    @Column(name = "base_quantity", nullable = false)
    private int baseQuantity;

    @Column(name = "extra_price_per_sample", nullable = false)
    private double extraPricePerSample;

    @Column(name = "service_type", nullable = false)
    private String serviceType;  // DAN_SU, HANH_CHINH

    public NumberSample() {}

    public NumberSample(int baseQuantity, double extraPricePerSample, String serviceType) {
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

    public void setBaseQuantity(int baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public double getExtraPricePerSample() {
        return extraPricePerSample;
    }

    public void setExtraPricePerSample(double extraPricePerSample) {
        this.extraPricePerSample = extraPricePerSample;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
