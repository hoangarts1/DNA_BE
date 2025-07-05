package com.ADNService.SWP391.dto;

import java.util.List;

public class TestResultPDFDTO {
    private TestOrderDTO testOrder;
    private List<TestResultDTO> testResult;
    private List<TestSampleDTO> testSamples;
    private List<TestResultSampleDTO> testResultSamples;
    // Getter, Setter, Constructor
    public TestResultPDFDTO() {
    }
    public TestResultPDFDTO(TestOrderDTO testOrder, List<TestResultDTO> testResult, List<TestSampleDTO> testSamples, List<TestResultSampleDTO> testResultSamples) {
        this.testOrder = testOrder;
        this.testResult = testResult;
        this.testSamples = testSamples;
        this.testResultSamples = testResultSamples;
    }

    public TestOrderDTO getTestOrder() {
        return testOrder;
    }

    public void setTestOrder(TestOrderDTO testOrder) {
        this.testOrder = testOrder;
    }

    public List<TestResultDTO> getTestResult() {
        return testResult;
    }

    public void setTestResult(List<TestResultDTO> testResult) {
        this.testResult = testResult;
    }

    public List<TestSampleDTO> getTestSamples() {
        return testSamples;
    }

    public void setTestSamples(List<TestSampleDTO> testSamples) {
        this.testSamples = testSamples;
    }

    public List<TestResultSampleDTO> getTestResultSamples() {
        return testResultSamples;
    }

    public void setTestResultSamples(List<TestResultSampleDTO> testResultSamples) {
        this.testResultSamples = testResultSamples;
    }
}
