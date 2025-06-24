package com.ADNService.SWP391.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_result_sample")
public class TestResultSample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_sample_id")
    private TestSample testSample;

    @Column(name = "locus_name", unique = true, columnDefinition = "NVARCHAR(255)")
    private String locusName;

    @Column(name = "allele1", unique = true, columnDefinition = "NVARCHAR(255)")
    private String allele1;

    @Column(name = "allele2", unique = true, columnDefinition = "NVARCHAR(255)")
    private String allele2;

    public TestResultSample() {
    }

    public TestResultSample(TestSample testSample, String locusName, String allele1, String allele2) {
        this.testSample = testSample;
        this.locusName = locusName;
        this.allele1 = allele1;
        this.allele2 = allele2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestSample getTestSample() {
        return testSample;
    }

    public void setTestSample(TestSample testSample) {
        this.testSample = testSample;
    }

    public String getLocusName() {
        return locusName;
    }

    public void setLocusName(String locusName) {
        this.locusName = locusName;
    }

    public String getAllele1() {
        return allele1;
    }

    public void setAllele1(String allele1) {
        this.allele1 = allele1;
    }

    public String getAllele2() {
        return allele2;
    }

    public void setAllele2(String allele2) {
        this.allele2 = allele2;
    }
}
