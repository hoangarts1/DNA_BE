package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.ServiceDTO;
import com.ADNService.SWP391.service.ServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    @Autowired
    private ServiceInterface serviceInterface;

    // CREATE
    @PostMapping
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO dto) {
        ServiceDTO created = serviceInterface.createService(dto);
        return ResponseEntity.ok(created);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable String id) {
        ServiceDTO service = serviceInterface.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServiceDTO> services = serviceInterface.getAllServices();
        return ResponseEntity.ok(services);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable String id, @RequestBody ServiceDTO dto) {
        ServiceDTO updated = serviceInterface.updateService(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable String id) {
        serviceInterface.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
