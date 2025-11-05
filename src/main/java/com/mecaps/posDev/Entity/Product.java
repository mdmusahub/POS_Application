package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String product_description;

    @Column(nullable = false , unique = true)

    private String sku;

    @ManyToOne
    private Category category_id;
    @ManyToOne
    private GstTax gst;

    @OneToMany(mappedBy = "product_id",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> product_variant = new ArrayList<>();

    @OneToMany(mappedBy = "product_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductInventory> inventories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @DateTimeFormat
    @CreationTimestamp
    private LocalDateTime created_at;

    @DateTimeFormat
    @UpdateTimestamp
    private LocalDateTime updated_at;
}
