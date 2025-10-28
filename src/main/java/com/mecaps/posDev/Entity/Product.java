package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @DateTimeFormat
    @CreationTimestamp
    private LocalDateTime created_at;


    @Column(length = 255)
    private String description;

    @Column(length = 255, unique = true, nullable = false)
    private String sku;

    @DateTimeFormat
    @UpdateTimestamp
    private LocalDateTime updated_at;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Category> category_id = new ArrayList<>();






}
