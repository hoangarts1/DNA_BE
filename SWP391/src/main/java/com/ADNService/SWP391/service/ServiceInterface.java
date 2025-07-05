package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.ServiceDTO;

import java.util.List;

public interface ServiceInterface{
    ServiceDTO createService(ServiceDTO dto);
    ServiceDTO getServiceById(String id);
    List<ServiceDTO> getAllServices();
    ServiceDTO updateService(String id, ServiceDTO dto);
    void deleteService(String id);
    double calculateTotalPrice(String serviceId, int numberOfSamples);
}
