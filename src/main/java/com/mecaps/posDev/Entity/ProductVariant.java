package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productVariantId;

    @Column(name = "variant_name", nullable = false)
    private String variantName;

    @Column(nullable = false)
    private Double product_variant_price;

    @Column(nullable = false)
    private Boolean refundable;

    @Column(nullable = false)
    private String product_variant_value;
    
    @ManyToOne
    @JoinColumn(name = "product_id")

    private Product productId;

    @OneToOne(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductInventory inventory;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Discount discount;


}
