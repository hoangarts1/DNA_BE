package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestOrderDTO;
import com.ADNService.SWP391.service.TestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testorders")
public class TestOrderController {

    @Autowired
    private TestOrderService testOrderService;

    @GetMapping
    public List<TestOrderDTO> getAll() {
        return testOrderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public TestOrderDTO getById(@PathVariable String id) {
        return testOrderService.getOrderById(id);
    }

    @PostMapping
    public ResponseEntity<TestOrderDTO> create(@RequestBody TestOrderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testOrderService.createOrder(dto));
    }

    @PutMapping("/{id}")
    public TestOrderDTO update(@PathVariable String id, @RequestBody TestOrderDTO dto) {
        return testOrderService.updateOrder(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        testOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TestOrderDTO>> getByCustomerId(@PathVariable Long customerId) {
        List<TestOrderDTO> orders = testOrderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
}
