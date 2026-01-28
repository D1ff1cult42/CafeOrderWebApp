package org.example.paymentservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.common.kafka.Status;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "yookassa_payment_id")
    private String yookassaPaymentId;

    @Column(name = "yookassa_confirmation_url")
    private String yookassaConfirmationUrl;

    @Column(unique = true)
    private String idempotenceKey;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @PrePersist
    public void prePersist() {
        createdAt = new Timestamp(System.currentTimeMillis());
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
