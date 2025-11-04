package com.mecaps.posDev.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_item_id;

    @Column(nullable = false)
    private Long quantity;

    private Double total_price;

    @Column(nullable = false)
    private Double unit_price;

    @ManyToOne
    private Product product;

    @ManyToOne
    private ProductVariant productVariant;

}
