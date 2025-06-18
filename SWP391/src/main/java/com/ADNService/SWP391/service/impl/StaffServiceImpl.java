package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.StaffDTO;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.entity.Customer;
import com.ADNService.SWP391.entity.Staff;
import com.ADNService.SWP391.enums.Role;
import com.ADNService.SWP391.repository.AccountRepository;
import com.ADNService.SWP391.repository.CustomerRepository;
import com.ADNService.SWP391.repository.StaffRepository;
import com.ADNService.SWP391.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public StaffDTO getStaffById(Long id) {
        return staffRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public StaffDTO getStaffByAccountId(Long accountId) {
        return staffRepository.findByAccountId(accountId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Staff with Account ID " + accountId + " does not exist."));
    }

    @Override
    public StaffDTO createStaff(StaffDTO dto) {
        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);
        if (account == null) {
            throw new RuntimeException("Account with ID " + dto.getAccountId() + " does not exist.");
        }

        Optional<Staff> existingStaff = staffRepository.findByAccountId(dto.getAccountId());
        if (existingStaff.isPresent()) {
            throw new RuntimeException("Account ID " + dto.getAccountId() + " existed before. Cannot create new Staff with ID .");
        }


        if (account.getRole() != Role.STAFF) {
            throw new RuntimeException("Cannot assign "+account.getRole()+" role to Staff");
        }

        Staff staff = new Staff();
        staff.setAccount(account);
        staff.setFingerprint(dto.getFingerprint());
        staff.setRole(dto.getRole());

        return convertToDTO(staffRepository.save(staff));
    }

    @Override
    public StaffDTO updateStaff(Long id, StaffDTO dto) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isEmpty()) {
            throw new RuntimeException("Staff with ID " + id + " does not exist.");
        }

        Staff staff = optionalStaff.get();
        staff.setFingerprint(dto.getFingerprint());
        staff.setRole(dto.getRole());

        return convertToDTO(staffRepository.save(staff));
    }

    @Override
    public void deleteStaff(Long id) {

        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (optionalStaff.isEmpty()) {
            throw new RuntimeException("Staff with ID " + id + " does not exist.");
        }
        staffRepository.deleteById(id);
    }
    private StaffDTO convertToDTO(Staff staff) {
        StaffDTO dto = new StaffDTO();
        dto.setId(staff.getId());
        dto.setAccountId(staff.getAccount().getId());
        dto.setFingerprint(staff.getFingerprint());
        dto.setRole(staff.getRole());
        return dto;
    }
}
