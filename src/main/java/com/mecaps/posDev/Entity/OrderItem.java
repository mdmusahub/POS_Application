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

    // 🧾 --- GST / Tax Details ---
    @Column(nullable = false)
    private Double gstRate;      // GST rate applied (e.g., 5, 12, 18, 28)

    @Column(nullable = false)
    private Double gstAmount;    // Total GST amount for this item (CGST + SGST)

    @Column(nullable = false)
    private Double c_gstAmount;   // Central GST part

    @Column(nullable = false)
    private Double s_gstAmount;   // State GST part

    // Optional: reference back to which GST rule was used
    @ManyToOne
    @JoinColumn(name = "id")
    private GstTax gstTax;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    @ManyToOne
    private ProductVariant productVariant;

}
