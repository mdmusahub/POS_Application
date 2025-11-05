package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long product_variant_id;

    @Column(nullable = false)
    private String variantName;

    @Column(nullable = false)
    private Double product_variant_price;

    @Column(nullable = false)
    private Boolean refundable;

    @Column(nullable = false)
    private String product_variant_value;
    
    @ManyToOne
    private Product product_id;

    @OneToOne(mappedBy = "product_variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductInventory inventory;

    @OneToMany(mappedBy = "product_variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "product_variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Discount discount;


}
