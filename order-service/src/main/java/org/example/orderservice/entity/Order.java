package org.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.common.kafka.Status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;

    @ElementCollection
    @CollectionTable(
            name = "order_pizza_id",
            joinColumns = @JoinColumn(name = "order_id")
    )
    @Column(name = "pizza_ids")
    private List<Long> pizzaIds;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;
}
