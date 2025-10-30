package com.mecaps.posDev.Entity;

import jakarta.persistence.*;

@Entity
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_items_id;

    @Column(nullable = false)
    private Long quantity;

    private Double total_price;

    private Double unit_price;

    @ManyToOne
    private Product product_id;



}
