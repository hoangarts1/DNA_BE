package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.TestSampleDTO;
import com.ADNService.SWP391.entity.*;
import com.ADNService.SWP391.repository.*;
import com.ADNService.SWP391.service.TestSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestSampleServiceImpl implements TestSampleService {

    @Autowired
    private TestSampleRepository testSampleRepository;

    @Autowired
    private TestOrderRepository testOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SampleTypeRepository sampleTypeRepository;

    @Override
    public TestSampleDTO createTestSample(TestSampleDTO dto) {
        if (dto.getOrderId() == null) {
            throw new IllegalArgumentException("ID đơn hàng là bắt buộc");
        }

        if (dto.getCustomerId() == null) {
            throw new IllegalArgumentException("ID khách hàng là bắt buộc");
        }

        TestOrder order = testOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Đơn hàng với ID " + dto.getOrderId() + " không tồn tại."));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Khách hàng với ID " + dto.getCustomerId() + " không tồn tại."));

        SampleType sampleType = null;
        if (dto.getSampleTypeId() != null) {
            sampleType = sampleTypeRepository.findById(dto.getSampleTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Loại mẫu với ID " + dto.getSampleTypeId() + " không tồn tại."));
        }

        TestSample sample = new TestSample();
        sample.setOrder(order);
        sample.setCustomer(customer);
        sample.setSampleType(sampleType); // Cho phép sampleType là null
        sample.setName(dto.getName());
        sample.setGender(dto.getGender());
        sample.setDateOfBirth(dto.getDateOfBirth());
        sample.setDocumentType(dto.getDocumentType());
        sample.setDocumentNumber(dto.getDocumentNumber());
        sample.setDateOfIssue(dto.getDateOfIssue());
        sample.setExpirationDate(dto.getExpirationDate());
        sample.setPlaceOfIssue(dto.getPlaceOfIssue());
        sample.setNationality(dto.getNationality());
        sample.setAddress(dto.getAddress());
        sample.setNumberOfSample(dto.getNumberOfSample());
        sample.setRelationship(dto.getRelationship());
        sample.setMedicalHistory(dto.getMedicalHistory());
        sample.setFingerprint(dto.getFingerprint());
        sample.setKitCode(dto.getKitCode());

        return convertToDTO(testSampleRepository.save(sample));
    }

    @Override
    public List<TestSampleDTO> getAllTestSamples() {
        return testSampleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TestSampleDTO getTestSampleById(Long id) {
        return testSampleRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<TestSampleDTO> getTestSamplesByOrderId(Long orderId) {
        return testSampleRepository.getTestSamplesByOrder_OrderId(orderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TestSampleDTO updateTestSample(Long id, TestSampleDTO dto) {
        TestSample sample = testSampleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mẫu xét nghiệm với ID " + id + " không tồn tại."));

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Khách hàng với ID " + dto.getCustomerId() + " không tồn tại."));
            sample.setCustomer(customer);
        }

        if (dto.getSampleTypeId() != null) {
            SampleType sampleType = sampleTypeRepository.findById(dto.getSampleTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("Loại mẫu với ID " + dto.getSampleTypeId() + " không tồn tại."));
            sample.setSampleType(sampleType);
        } else {
            sample.setSampleType(null); // Cho phép sampleType là null
        }

        sample.setName(dto.getName());
        sample.setGender(dto.getGender());
        sample.setDateOfBirth(dto.getDateOfBirth());
        sample.setDocumentType(dto.getDocumentType());
        sample.setDocumentNumber(dto.getDocumentNumber());
        sample.setDateOfIssue(dto.getDateOfIssue());
        sample.setExpirationDate(dto.getExpirationDate());
        sample.setPlaceOfIssue(dto.getPlaceOfIssue());
        sample.setNationality(dto.getNationality());
        sample.setAddress(dto.getAddress());
        sample.setNumberOfSample(dto.getNumberOfSample());
        sample.setRelationship(dto.getRelationship());
        sample.setMedicalHistory(dto.getMedicalHistory());
        sample.setFingerprint(dto.getFingerprint());
        sample.setKitCode(dto.getKitCode());

        return convertToDTO(testSampleRepository.save(sample));
    }

    @Override
    public void deleteTestSample(Long id) {
        if (!testSampleRepository.existsById(id)) {
            throw new IllegalArgumentException("Mẫu xét nghiệm với ID " + id + " không tồn tại.");
        }
        testSampleRepository.deleteById(id);
    }

    private TestSampleDTO convertToDTO(TestSample sample) {
        TestSampleDTO dto = new TestSampleDTO();
        dto.setId(sample.getId());
        dto.setOrderId(sample.getOrder() != null ? sample.getOrder().getOrderId() : null);
        dto.setCustomerId(sample.getCustomer() != null ? sample.getCustomer().getId() : null);
        dto.setSampleTypeId(sample.getSampleType() != null ? sample.getSampleType().getId() : null);
        dto.setName(sample.getName());
        dto.setGender(sample.getGender());
        dto.setDateOfBirth(sample.getDateOfBirth());
        dto.setDocumentType(sample.getDocumentType());
        dto.setDocumentNumber(sample.getDocumentNumber());
        dto.setDateOfIssue(sample.getDateOfIssue());
        dto.setExpirationDate(sample.getExpirationDate());
        dto.setPlaceOfIssue(sample.getPlaceOfIssue());
        dto.setNationality(sample.getNationality());
        dto.setAddress(sample.getAddress());
        dto.setNumberOfSample(sample.getNumberOfSample());
        dto.setRelationship(sample.getRelationship());
        dto.setMedicalHistory(sample.getMedicalHistory());
        dto.setFingerprint(sample.getFingerprint());
        dto.setKitCode(sample.getKitCode());
        return dto;
    }
}