package com.ADNService.SWP391.service.impl;

import com.ADNService.SWP391.dto.CustomerDTO;
import com.ADNService.SWP391.entity.Account;
import com.ADNService.SWP391.entity.Customer;
import com.ADNService.SWP391.enums.Role;
import com.ADNService.SWP391.repository.AccountRepository;
import com.ADNService.SWP391.repository.CustomerRepository;
import com.ADNService.SWP391.service.CustomerService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Override
    public CustomerDTO getCustomerByAccountId(Long accountId) {
        return customerRepository.findByAccountId(accountId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Customer with Account ID " + accountId + " does not exist."));
    }
    
    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);
        if (account == null) {
            throw new RuntimeException("Account with ID " + dto.getAccountId() + " does not exist.");
        }

        Optional<Customer> existingCustomer = customerRepository.findByAccountId(dto.getAccountId());
        if (existingCustomer.isPresent()) {
            throw new RuntimeException("Account ID " + dto.getAccountId() + " existed before. Cannot create new Customer with ID .");
        }

        if (account.getRole() != Role.CUSTOMER) {
            throw new RuntimeException("Cannot assign "+account.getRole()+" role to Customer");
        }

        Customer customer = new Customer();

        customer.setAccount(account);
        customer.setAddress(dto.getAddress());
        customer.setGender(dto.getGender());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setDocumentType(dto.getDocumentType());
        customer.setPlaceOfIssue(dto.getPlaceOfIssue());
        customer.setDateOfIssue(dto.getDateOfIssue());
        customer.setFingerprint(dto.getFingerprint());

        return convertToDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer with ID " + id + " does not exist.");
        }

        Customer customer = optionalCustomer.get();
        customer.setAddress(dto.getAddress());
        customer.setGender(dto.getGender());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setDocumentType(dto.getDocumentType());
        customer.setPlaceOfIssue(dto.getPlaceOfIssue());
        customer.setDateOfIssue(dto.getDateOfIssue());
        customer.setFingerprint(dto.getFingerprint());

        return convertToDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {

    Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
        throw new RuntimeException("Customer with ID " + id + " does not exist.");
    }
        customerRepository.deleteById(id);
}
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setAccountId(customer.getAccount().getId());
        dto.setAddress(customer.getAddress());
        dto.setGender(customer.getGender());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setDocumentType(customer.getDocumentType());
        dto.setPlaceOfIssue(customer.getPlaceOfIssue());
        dto.setDateOfIssue(customer.getDateOfIssue());
        dto.setFingerprint(customer.getFingerprint());
        return dto;
    }
}
