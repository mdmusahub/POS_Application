package com.mecaps.posDev.Entity;

import com.mecaps.posDev.Enums.OrderStatus;
import com.mecaps.posDev.Enums.PaymentMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ordering")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private PaymentMode payment_mode;

    private String razorpayOrderId;

    private String cash_amount;
    private String online_amount;

    @Enumerated(EnumType.STRING)
    private OrderStatus order_status;

    private Double discount;
    private Double tax;
    private Double total_amount;

    @ManyToOne
    private Customer customer;
    private String user_phone_number;
    private String user_email;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> order_items = new ArrayList<>();

    @DateTimeFormat
    @CreationTimestamp
    private LocalDateTime order_date;

    @DateTimeFormat
    @UpdateTimestamp
    private LocalDateTime update_at;

}
