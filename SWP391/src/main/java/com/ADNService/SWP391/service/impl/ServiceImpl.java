package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.ServiceDTO;
import com.ADNService.SWP391.entity.Services;
import com.ADNService.SWP391.exception.CustomException;
import com.ADNService.SWP391.repository.ServiceRepository;
import com.ADNService.SWP391.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceImpl implements ServiceInterface {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public ServiceDTO createService(ServiceDTO dto) {
        Services service = new Services();
        service.setServiceName(dto.getServiceName());
        service.setServiceType(dto.getServiceType());
        service.setTimeTest(dto.getTimeTest());
        service.setDescribe(dto.getDescribe());
        service.setNumberOfSamples(dto.getNumberOfSamples() != 0 ? dto.getNumberOfSamples() : 2);
        service.setPricePerAdditionalSample(dto.getPricePerAdditionalSample());
        service.setServicePrice(dto.getPrice());
        service.setActive(dto.isActive());

        Services savedService = serviceRepository.save(service);

        return mapToDTO(savedService);
    }

    @Override
    public ServiceDTO getServiceById(String id) {
        Services service = serviceRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomException("Service not found with ID: " + id));

        return mapToDTO(service);
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        List<Services> services = serviceRepository.findAll();
        return services.stream().map(this::mapToDTO).toList();
    }

    @Override
    public ServiceDTO updateService(String id, ServiceDTO dto) {
        Services service = serviceRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomException("Service not found with ID: " + id));

        service.setServiceName(dto.getServiceName());
        service.setServiceType(dto.getServiceType());
        service.setTimeTest(dto.getTimeTest());
        service.setDescribe(dto.getDescribe());
        service.setNumberOfSamples(dto.getNumberOfSamples() != 0 ? dto.getNumberOfSamples() : 2);
        service.setPricePerAdditionalSample(dto.getPricePerAdditionalSample());
        service.setServicePrice(dto.getPrice());
        service.setActive(dto.isActive());

        Services updated = serviceRepository.save(service);

        return mapToDTO(updated);
    }

    @Override
    public void deleteService(String id) {
        Services service = serviceRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomException("Service not found with ID: " + id));

        serviceRepository.delete(service);
    }

    private ServiceDTO mapToDTO(Services service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setServiceID(service.getServiceID());
        dto.setServiceName(service.getServiceName());
        dto.setServiceType(service.getServiceType());
        dto.setTimeTest(service.getTimeTest());
        dto.setDescribe(service.getDescribe());
        dto.setPrice(service.getServicePrice());
        dto.setNumberOfSamples(service.getNumberOfSamples());
        dto.setPricePerAdditionalSample(service.getPricePerAdditionalSample());
        dto.setActive(service.isActive());
        return dto;
    }

    @Override
    public void toggleServiceActive(String id) {
        Services service = serviceRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomException("Service not found with ID: " + id));
        service.setActive(!service.isActive()); // Đảo ngược trạng thái
        serviceRepository.save(service);
    }


    @Override
    public double calculateTotalPrice(String serviceId, int numberOfSamples) {
        if (numberOfSamples < 0) {
            throw new CustomException("Number of samples must be non-negative");
        }
        Services service = serviceRepository.findById(Long.valueOf(serviceId))
                .orElseThrow(() -> new CustomException("Service not found with ID: " + serviceId));

        double totalPrice = service.getServicePrice();
        if (numberOfSamples > 2 && service.getPricePerAdditionalSample() != null) {
            totalPrice += (numberOfSamples - 2) * service.getPricePerAdditionalSample();
        }
        return totalPrice;
    }
}