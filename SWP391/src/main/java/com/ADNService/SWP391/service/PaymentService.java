package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    PaymentDTO create(PaymentDTO dto);
    PaymentDTO getById(Long id);
    List<PaymentDTO> getAll();
    PaymentDTO update(Long id, PaymentDTO dto);
    void delete(Long id);
}
