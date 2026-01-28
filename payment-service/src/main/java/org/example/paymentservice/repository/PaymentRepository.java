package org.example.paymentservice.repository;

import org.example.common.kafka.Status;
import org.example.paymentservice.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Boolean existsByOrderIdAndStatus(Long orderId, Status status);
    Page<Payment> findByOrderId(Long orderId, Pageable pageable);
    Optional<Payment> findByYookassaPaymentId(String yookassaPaymentId);
}
