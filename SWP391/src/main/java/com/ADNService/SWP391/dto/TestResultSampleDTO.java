package com.ADNService.SWP391.dto;

public class TestResultSampleDTO {
    private Long id;
    private Long testSampleId;
    private String locusName;
    private String allele1;
    private String allele2;

    public TestResultSampleDTO() {
    }

    public TestResultSampleDTO(Long id, Long testSampleId, String locusName, String allele1, String allele2) {
        this.id = id;
        this.testSampleId = testSampleId;
        this.locusName = locusName;
        this.allele1 = allele1;
        this.allele2 = allele2;
    }

    // Getter và Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestSampleId() {
        return testSampleId;
    }

    public void setTestSampleId(Long testSampleId) {
        this.testSampleId = testSampleId;
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
