package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.CustomerDTO;
import com.ADNService.SWP391.dto.TestSampleDTO;
import com.ADNService.SWP391.entity.*;
import com.ADNService.SWP391.repository.*;
import com.ADNService.SWP391.service.TestSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        if (dto.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID is required");
        }
        if (dto.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        TestOrder order = testOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order with ID " + dto.getOrderId() + " does not exist."));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer with ID " + dto.getCustomerId() + " does not exist."));

        Staff staff = null;
        if (dto.getStaffId() != null) {
            staff = staffRepository.findById(dto.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff with ID " + dto.getStaffId() + " does not exist."));
        }

//        Optional<TestSample> existingSample = testSampleRepository.findById(dto.getOrderId());
//        if (existingSample.isPresent()) {
//            throw new RuntimeException("Test Sample with Order ID " + dto.getOrderId() + " already exists.");
//        }

        TestSample sample = new TestSample();
        sample.setOrder(order);
        sample.setCustomer(customer);
        sample.setStaff(staff);
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
        Optional<TestSample> optionalTestSample = testSampleRepository.findById(id);
        if (optionalTestSample.isEmpty()) {
            throw new RuntimeException("Test Sample with ID " + id + " does not exist.");
        }

        TestSample sample = optionalTestSample.get();

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer with ID " + dto.getCustomerId() + " does not exist."));
            sample.setCustomer(customer);
        }

        if (dto.getStaffId() != null) {
            Staff staff = staffRepository.findById(dto.getStaffId())
                    .orElseThrow(() -> new RuntimeException("Staff with ID " + dto.getStaffId() + " does not exist."));
            sample.setStaff(staff);
        } else {
            sample.setStaff(null);
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
        Optional<TestSample> sample = testSampleRepository.findById(id);
        if (sample.isEmpty()) {
            throw new RuntimeException("Test Sample with ID " + id + " does not exist.");
        }
        testSampleRepository.deleteById(id);
    }


    private TestSampleDTO convertToDTO(TestSample sample) {
        TestSampleDTO dto = new TestSampleDTO();
        dto.setId(sample.getId());
        dto.setOrderId(sample.getOrder() != null ? sample.getOrder().getOrderId() : null);
        dto.setCustomerId(sample.getCustomer() != null ? sample.getCustomer().getId() : null);
        dto.setStaffId(sample.getStaff() != null ? sample.getStaff().getId() : null);
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
        dto.setSampleType(sample.getSampleType());
        dto.setNumberOfSample(sample.getNumberOfSample());
        dto.setRelationship(sample.getRelationship());
        dto.setMedicalHistory(sample.getMedicalHistory());
        dto.setFingerprint(sample.getFingerprint());

        return dto;
    }

    @Override
    public List<TestSampleDTO> getTestSamplesByOrderId(Long orderId) {
        List<TestSample> samples = testSampleRepository.findByOrder_OrderId(orderId);
        return samples.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}