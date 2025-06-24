package com.ADNService.SWP391.controller;

import com.ADNService.SWP391.dto.TestOrderDTO;
import com.ADNService.SWP391.dto.UpdateTestOrderStatusDTO;
import com.ADNService.SWP391.entity.Staff;
import com.ADNService.SWP391.entity.TestOrder;
import com.ADNService.SWP391.repository.StaffRepository;
import com.ADNService.SWP391.repository.TestOrderRepository;
import com.ADNService.SWP391.service.TestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst().map(auth -> auth.getAuthority().replace("ROLE_", "")).orElse("");

        if ("CUSTOMER".equals(role)) {
            if (!"SEND_SAMPLE".equals(dto.getOrderStatus())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("CUSTOMER chỉ được cập nhật trạng thái SEND_SAMPLE");
            }
            order.setOrderStatus(dto.getOrderStatus());
            testOrderRepository.save(order);
            return ResponseEntity.ok("Cập nhật thành công");
        }

        // 🎯 Phân quyền STAFF
        if ("NORMAL_STAFF".equals(role)) {
            if (!List.of("PENDING", "SEND_KIT", "COLLECT_SAMPLE", "COMPLETED").contains(dto.getOrderStatus())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("NORMAL_STAFF không được cập nhật trạng thái " + dto.getOrderStatus());
            }
        } else if ("LAB_STAFF".equals(role)) {
            if (!List.of("TESTED").contains(dto.getOrderStatus())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("LAB_STAFF không được cập nhật trạng thái " + dto.getOrderStatus());
            }
        }

        order.setOrderStatus(dto.getOrderStatus());
        Staff staff = staffRepository.findById(dto.getStaffId()).orElse(null);
        if (staff == null) return ResponseEntity.badRequest().body("Không tìm thấy staff");

        if (List.of("PENDING", "SEND_KIT", "COLLECT_SAMPLE", "COMPLETED").contains(dto.getOrderStatus())) {
            order.setRegistrationStaff(staff);
        } else if ("TESTED".equals(dto.getOrderStatus())) {
            order.setTestingStaff(staff);
        }

        testOrderRepository.save(order);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @GetMapping("/{id}/export-pdf")
    public ResponseEntity<byte[]> exportTestOrderPdf(@PathVariable Long id) throws IOException {
        byte[] pdfBytes = testOrderService.generateTestOrderPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("TestOrder_" + id + ".pdf").build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}