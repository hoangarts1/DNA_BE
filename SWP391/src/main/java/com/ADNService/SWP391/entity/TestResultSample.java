package com.ADNService.SWP391.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TestResultSample")
public class TestResultSample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "test_sample_id")
    private TestSample testSample;

    @Column(nullable = false)
    private String amelogenin;

    @Column(nullable = false)
    private String d3S1358;

    @Column(nullable = false)
    private String d2S441;

    @Column(nullable = false)
    private String d10S1248;

    @Column(nullable = false)
    private String d13S317;

    @Column(nullable = false)
    private String d16S539;

    @Column(nullable = false)
    private String csf1po;

    @Column(nullable = false)
    private String th01;

    @Column(nullable = false)
    private String vwa;

    @Column(nullable = false)
    private String d7S820;

    @Column(nullable = false)
    private String d21S11;

    @Column(nullable = false)
    private String pentaE;

    @Column(nullable = false)
    private String fga;

    @Column(nullable = false)
    private String d22S1045;

    private String d8S1179;

    private String d18S51;

    @Column(name = "pentaD")
    private String pentaD;

    private String d2S1339;

    private String d19S433;

    private String d5S818;

    private String d1S1656;

    private String tpox;

    public TestResultSample() {
    }

    public TestResultSample(Long id, TestSample testSample, String amelogenin, String d3S1358, String d2S441, String d10S1248, String d13S317, String d16S539, String csf1po, String th01, String vwa, String d7S820, String d21S11, String pentaE, String fga, String d22S1045, String d8S1179, String d18S51, String pentaD, String d2S1339, String d19S433, String d5S818, String d1S1656, String tpox) {
        this.id = id;
        this.testSample = testSample;
        this.amelogenin = amelogenin;
        this.d3S1358 = d3S1358;
        this.d2S441 = d2S441;
        this.d10S1248 = d10S1248;
        this.d13S317 = d13S317;
        this.d16S539 = d16S539;
        this.csf1po = csf1po;
        this.th01 = th01;
        this.vwa = vwa;
        this.d7S820 = d7S820;
        this.d21S11 = d21S11;
        this.pentaE = pentaE;
        this.fga = fga;
        this.d22S1045 = d22S1045;
        this.d8S1179 = d8S1179;
        this.d18S51 = d18S51;
        this.pentaD = pentaD;
        this.d2S1339 = d2S1339;
        this.d19S433 = d19S433;
        this.d5S818 = d5S818;
        this.d1S1656 = d1S1656;
        this.tpox = tpox;
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

    public String getAmelogenin() {
        return amelogenin;
    }

    public void setAmelogenin(String amelogenin) {
        this.amelogenin = amelogenin;
    }

    public String getD3S1358() {
        return d3S1358;
    }

    public void setD3S1358(String d3S1358) {
        this.d3S1358 = d3S1358;
    }

    public String getD2S441() {
        return d2S441;
    }

    public void setD2S441(String d2S441) {
        this.d2S441 = d2S441;
    }

    public String getD10S1248() {
        return d10S1248;
    }

    public void setD10S1248(String d10S1248) {
        this.d10S1248 = d10S1248;
    }

    public String getD13S317() {
        return d13S317;
    }

    public void setD13S317(String d13S317) {
        this.d13S317 = d13S317;
    }

    public String getD16S539() {
        return d16S539;
    }

    public void setD16S539(String d16S539) {
        this.d16S539 = d16S539;
    }

    public String getCsf1po() {
        return csf1po;
    }

    public void setCsf1po(String csf1po) {
        this.csf1po = csf1po;
    }

    public String getTh01() {
        return th01;
    }

    public void setTh01(String th01) {
        this.th01 = th01;
    }

    public String getVwa() {
        return vwa;
    }

    public void setVwa(String vwa) {
        this.vwa = vwa;
    }

    public String getD7S820() {
        return d7S820;
    }

    public void setD7S820(String d7S820) {
        this.d7S820 = d7S820;
    }

    public String getD21S11() {
        return d21S11;
    }

    public void setD21S11(String d21S11) {
        this.d21S11 = d21S11;
    }

    public String getPentaE() {
        return pentaE;
    }

    public void setPentaE(String pentaE) {
        this.pentaE = pentaE;
    }

    public String getFga() {
        return fga;
    }

    public void setFga(String fga) {
        this.fga = fga;
    }

    public String getD22S1045() {
        return d22S1045;
    }

    public void setD22S1045(String d22S1045) {
        this.d22S1045 = d22S1045;
    }

    public String getD8S1179() {
        return d8S1179;
    }

    public void setD8S1179(String d8S1179) {
        this.d8S1179 = d8S1179;
    }

    public String getD18S51() {
        return d18S51;
    }

    public void setD18S51(String d18S51) {
        this.d18S51 = d18S51;
    }

    public String getPentaD() {
        return pentaD;
    }

    public void setPentaD(String pentaD) {
        this.pentaD = pentaD;
    }

    public String getD2S1339() {
        return d2S1339;
    }

    public void setD2S1339(String d2S1339) {
        this.d2S1339 = d2S1339;
    }

    public String getD19S433() {
        return d19S433;
    }

    public void setD19S433(String d19S433) {
        this.d19S433 = d19S433;
    }

    public String getD5S818() {
        return d5S818;
    }

    public void setD5S818(String d5S818) {
        this.d5S818 = d5S818;
    }

    public String getD1S1656() {
        return d1S1656;
    }

    public void setD1S1656(String d1S1656) {
        this.d1S1656 = d1S1656;
    }

    public String getTpox() {
        return tpox;
    }

    public void setTpox(String tpox) {
        this.tpox = tpox;
    }
}
