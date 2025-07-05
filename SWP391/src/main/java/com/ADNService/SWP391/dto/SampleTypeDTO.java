package com.ADNService.SWP391.dto;

public class SampleTypeDTO {
    private Long id;
    private String sampleType;

    public SampleTypeDTO() {}

    public SampleTypeDTO(Long id, String sampleType) {
        this.id = id;
        this.sampleType = sampleType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }
}
