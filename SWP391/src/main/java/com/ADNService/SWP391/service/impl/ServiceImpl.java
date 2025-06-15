package com.ADNService.SWP391.service.impl;
import com.ADNService.SWP391.entity.Services;

import com.ADNService.SWP391.dto.ServiceDTO;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.exception.CustomException;
import com.ADNService.SWP391.repository.ServiceRepository;
import com.ADNService.SWP391.repository.AccountRepository;
import com.ADNService.SWP391.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ServiceImpl implements ServiceInterface {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public ServiceDTO createService(ServiceDTO dto) {

        Services service = new Services();

        service.setServiceID(dto.getServiceID());

        service.setServiceName(dto.getServiceName());
        service.setServicePurpose(dto.getServicePurpose());
        service.setTimeTest(dto.getTimeTest());
        service.setServiceBlog(dto.getServiceBlog());
        service.setServicePrice(dto.getPrice());
        service.setNumberOfSample(dto.getNumberOfSample());

        Services savedService = serviceRepository.save(service);

        ServiceDTO result = new ServiceDTO();
        result.setServiceID(savedService.getServiceID());
        result.setServiceName(savedService.getServiceName());
        result.setServicePurpose(savedService.getServicePurpose());
        result.setTimeTest(savedService.getTimeTest());
        result.setServiceBlog(savedService.getServiceBlog());
        result.setPrice(savedService.getServicePrice());
        result.setNumberOfSample(savedService.getNumberOfSample());

        return result;
    }


    @Override
    public ServiceDTO getServiceById(String id) {
        Services service = serviceRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomException("Service not found with ID: " + id));

        ServiceDTO dto = new ServiceDTO();
        dto.setServiceID(service.getServiceID());
        dto.setServiceName(service.getServiceName());
        dto.setServicePurpose(service.getServicePurpose());
        dto.setTimeTest(service.getTimeTest());
        dto.setServiceBlog(service.getServiceBlog());
        dto.setPrice(service.getServicePrice());
        dto.setNumberOfSample(service.getNumberOfSample());

        return dto;
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        List<Services> services = serviceRepository.findAll();

        return services.stream().map(service -> {
            ServiceDTO dto = new ServiceDTO();
            dto.setServiceID(service.getServiceID());
            dto.setServiceName(service.getServiceName());
            dto.setServicePurpose(service.getServicePurpose());
            dto.setTimeTest(service.getTimeTest());
            dto.setServiceBlog(service.getServiceBlog());
            dto.setPrice(service.getServicePrice());
            dto.setNumberOfSample(service.getNumberOfSample());
            return dto;
        }).toList();
    }

    @Override
    public ServiceDTO updateService(String id, ServiceDTO dto) {
        Services service = serviceRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomException("Service not found with ID: " + id));

        service.setServiceName(dto.getServiceName());
        service.setServicePurpose(dto.getServicePurpose());
        service.setTimeTest(dto.getTimeTest());
        service.setServiceBlog(dto.getServiceBlog());
        service.setServicePrice(dto.getPrice());
        service.setNumberOfSample(dto.getNumberOfSample());

        Services updated = serviceRepository.save(service);

        ServiceDTO result = new ServiceDTO();
        result.setServiceID(updated.getServiceID());
        result.setServiceName(updated.getServiceName());
        result.setServicePurpose(updated.getServicePurpose());
        result.setTimeTest(updated.getTimeTest());
        result.setServiceBlog(updated.getServiceBlog());
        result.setPrice(updated.getServicePrice());
        result.setNumberOfSample(updated.getNumberOfSample());

        return result;
    }

    @Override
    public void deleteService(String id) {
        Services service = serviceRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomException("Service not found with ID: " + id));

        serviceRepository.delete(service);
    }
}
