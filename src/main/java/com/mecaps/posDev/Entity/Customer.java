package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customer_id;

    @DateTimeFormat
    @CreationTimestamp
    private LocalDateTime created_at;

    @Column(nullable = false)
    private String email;

    private String customer_phone_number;

    @OneToMany(mappedBy = "customer_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ordering order;
}
