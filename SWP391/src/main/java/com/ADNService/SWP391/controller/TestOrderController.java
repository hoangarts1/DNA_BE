package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestOrderDTO;
import com.ADNService.SWP391.dto.UpdateTestOrderStatusDTO;
import com.ADNService.SWP391.entity.Staff;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.repository.StaffRepository;
import com.ADNService.SWP391.repository.TestOrderRepository;
import com.ADNService.SWP391.service.TestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/testorders")
public class TestOrderController {

    @Autowired
    private TestOrderService testOrderService;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private TestOrderRepository testOrderRepository;

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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateTestOrderStatusDTO dto
    ) {
        Optional<TestOrder> opt = testOrderRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        TestOrder order = opt.get();
        order.setOrderStatus(dto.getOrderStatus());

        Staff staff = staffRepository.findById(dto.getStaffId()).orElse(null);
        if (staff == null) return ResponseEntity.badRequest().body("Không tìm thấy staff");

        // Phân quyền gán đúng vai trò
        if (List.of("PENDING", "PREPARING", "COLLECTING", "TRANSFERRING").contains(dto.getOrderStatus())) {
            order.setRegistrationStaff(staff);
        } else if (List.of("TESTING", "COMPLETED").contains(dto.getOrderStatus())) {
            order.setTestingStaff(staff);
        }

        testOrderRepository.save(order);
        return ResponseEntity.ok("Cập nhật thành công");
    }

}
