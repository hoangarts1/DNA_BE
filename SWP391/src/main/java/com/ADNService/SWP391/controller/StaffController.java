package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.StaffDTO;
import com.ADNService.SWP391.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public List<StaffDTO> getAllStaff() {
        return staffService.getAllStaff();
    }

    @GetMapping("/{id}")
    public StaffDTO getStaffById(@PathVariable Long id) {
        return staffService.getStaffById(id);
    }

    @PostMapping
    public StaffDTO createStaff(@RequestBody StaffDTO dto) {
        return staffService.createStaff(dto);
    }

    @PutMapping("/{id}")
    public StaffDTO updateStaff(@PathVariable Long id, @RequestBody StaffDTO dto) {
        return staffService.updateStaff(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
    }
}
