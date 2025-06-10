package com.ADNService.SWP391.service;

import com.ADNService.SWP391.dto.StaffDTO;

import java.util.List;

public interface StaffService {
    List<StaffDTO> getAllStaff();
    StaffDTO getStaffById(Long id);
    StaffDTO createStaff(StaffDTO staffDTO);
    StaffDTO updateStaff(Long id, StaffDTO staffDTO);
    void deleteStaff(Long id);
}
