package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "SampleType")
public class SampleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sample_type", nullable = false, columnDefinition = "NVARCHAR(255)", unique = true)
    private String sampleType;

    @OneToMany(mappedBy = "sampleType", cascade = CascadeType.ALL)
    private List<TestSample> testSamples;

    public SampleType() {}

    public SampleType(Long id, String sampleType, List<TestSample> testSamples) {
        this.id = id;
        this.sampleType = sampleType;
        this.testSamples = testSamples;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }

    public List<TestSample> getTestSamples() { return testSamples; }
    public void setTestSamples(List<TestSample> testSamples) { this.testSamples = testSamples; }
}

