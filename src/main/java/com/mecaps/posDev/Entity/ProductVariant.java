package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productVariantId;

    @Column(name = "variant_name", nullable = false)
    private String variantName;

    @Column(nullable = false)
    private Double productVariantPrice;

    @Column(nullable = false)
    private Boolean refundable;

    @Column(nullable = false)
    private String productVariantValue;

    @ManyToOne
    private Product productId;

    @OneToOne(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductInventory> inventory = new ArrayList<>();

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Discount discount;


}
