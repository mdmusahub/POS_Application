package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
public class ReturnOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long return_order_id;

    @Column(nullable = false)
    private Double refund_amount;

    @DateTimeFormat
    @CreationTimestamp
    private LocalDateTime return_date;

    @Column(nullable = false)
    private Long return_quantity;

    @ManyToOne
    private Order order_id;

    @ManyToOne
    private Customer requested_by_customer_id;
}
