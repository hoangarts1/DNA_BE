//package com.ADNService.SWP391.repository;
//
//import com.ADNService.SWP391.entity.Payment;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface PaymentRepository extends JpaRepository<Payment, Long> {
//    List<Payment> findByOrder_Customer_IdOrOrderRent_Customer_Id(Long orderCustomerId, Long orderRentCustomerId);
//}
