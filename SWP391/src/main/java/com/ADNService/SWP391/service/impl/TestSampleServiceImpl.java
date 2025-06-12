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
    private StaffRepository staffRepository;

    @Override
    public TestSampleDTO createTestSample(TestSampleDTO dto) {
        TestOrder order = testOrderRepository.findById(dto.getOrderId()).orElse(null);
        Customer customer = customerRepository.findById(dto.getCustomerId()).orElse(null);
        Staff staff = staffRepository.findById(dto.getStaffId()).orElse(null);

        TestSample sample = new TestSample();
        sample.setOrder(order);
        sample.setCustomer(customer);
        sample.setStaff(staff);
        sample.setName(dto.getName());
        sample.setDateOfBirth(dto.getDateOfBirth());
        sample.setDocumentType(dto.getDocumentType());
        sample.setDocumentNumber(dto.getDocumentNumber());
        sample.setDateOfIssue(dto.getDateOfIssue());
        sample.setExpirationDate(dto.getExpirationDate());
        sample.setPlaceOfIssue(dto.getPlaceOfIssue());
        sample.setNationality(dto.getNationality());
        sample.setAddress(dto.getAddress());
        sample.setSampleType(dto.getSampleType());
        sample.setNumberOfSample(dto.getNumberOfSample());
        sample.setRelationship(dto.getRelationship());
        sample.setMedicalHistory(dto.getMedicalHistory());
        sample.setFingerprint(dto.getFingerprint());

        TestSample savedSample = testSampleRepository.save(sample);
        return convertToDTO(savedSample);
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
    public TestSampleDTO updateTestSample(Long id, TestSampleDTO dto) {
        TestSample sample = testSampleRepository.findById(id).orElse(null);
        if (sample == null) return null;

        sample.setName(dto.getName());
        sample.setDateOfBirth(dto.getDateOfBirth());
        sample.setDocumentType(dto.getDocumentType());
        sample.setDocumentNumber(dto.getDocumentNumber());
        sample.setDateOfIssue(dto.getDateOfIssue());
        sample.setExpirationDate(dto.getExpirationDate());
        sample.setPlaceOfIssue(dto.getPlaceOfIssue());
        sample.setNationality(dto.getNationality());
        sample.setAddress(dto.getAddress());
        sample.setSampleType(dto.getSampleType());
        sample.setNumberOfSample(dto.getNumberOfSample());
        sample.setRelationship(dto.getRelationship());
        sample.setMedicalHistory(dto.getMedicalHistory());
        sample.setFingerprint(dto.getFingerprint());

        TestSample updatedSample = testSampleRepository.save(sample);
        return convertToDTO(updatedSample);
    }

    @Override
    public void deleteTestSample(Long id) {
        testSampleRepository.deleteById(id);
    }

    private TestSampleDTO convertToDTO(TestSample sample) {
        TestSampleDTO dto = new TestSampleDTO();
        dto.setId(sample.getId());
        dto.setOrderId(sample.getOrder() != null ? sample.getOrder().getOrderId() : null);
        dto.setCustomerId(sample.getCustomer() != null ? sample.getCustomer().getId() : null);
        dto.setStaffId(sample.getStaff() != null ? sample.getStaff().getId() : null);
        dto.setName(sample.getName());
        dto.setDateOfBirth(sample.getDateOfBirth());
        dto.setDocumentType(sample.getDocumentType());
        dto.setDocumentNumber(sample.getDocumentNumber());
        dto.setDateOfIssue(sample.getDateOfIssue());
        dto.setExpirationDate(sample.getExpirationDate());
        dto.setPlaceOfIssue(sample.getPlaceOfIssue());
        dto.setNationality(sample.getNationality());
        dto.setAddress(sample.getAddress());
        dto.setSampleType(sample.getSampleType());
        dto.setNumberOfSample(sample.getNumberOfSample());
        dto.setRelationship(sample.getRelationship());
        dto.setMedicalHistory(sample.getMedicalHistory());
        dto.setFingerprint(sample.getFingerprint());

        return dto;
    }
}
