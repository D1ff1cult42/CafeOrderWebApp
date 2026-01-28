package org.example.common.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.common.kafka.Status;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentEvent {
    private String eventId;
    private Long orderId;
    private Status paymentStatus;
    private Timestamp createdAt;

}