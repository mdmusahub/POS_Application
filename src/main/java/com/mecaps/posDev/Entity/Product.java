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
    private String product_name;
    private String product_description;
    private String sku;

    @ManyToOne
    private Category category_id;

    @OneToMany(mappedBy = "product_variant_id",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> product_variants = new ArrayList<>();

    @OneToMany(mappedBy = "product_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductInventory> inventories = new ArrayList<>();

    @DateTimeFormat
    @CreationTimestamp
    private LocalDateTime created_at;

    @DateTimeFormat
    @UpdateTimestamp
    private LocalDateTime updated_at;
}
