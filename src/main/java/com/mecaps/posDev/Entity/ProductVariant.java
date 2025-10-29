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
    private String product_variant_name;
    private Double product_variant_price;
    private Boolean refundable;
    private String product_variant_value;
    
    @ManyToOne
    private ProductVariant productVariant;

    @OneToOne(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductInventory> inventories = new ArrayList<>();

}
