package com.mecaps.posDev.Entity;

import com.mecaps.posDev.Entity.enums.OrderStatus;
import com.mecaps.posDev.Entity.enums.PaymentMode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Ordering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )

    private Long order_id;

    private String cash_amount;

    private Double discount;

    private String online_amount;

    @DateTimeFormat
    @CreationTimestamp
    private LocalDateTime order_date;

    @Enumerated(EnumType.STRING)
    private PaymentMode payment_mode;

    @Enumerated(EnumType.STRING)
    private OrderStatus order_status;

    private Double tax;

    private Double total_amount;

    @DateTimeFormat
    @UpdateTimestamp
    private LocalDateTime updated_at;


//    private String user_phone_number;

    @ManyToOne
    private Customer customer_id;



}
