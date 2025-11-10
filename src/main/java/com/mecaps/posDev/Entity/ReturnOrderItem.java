package com.mecaps.posDev.Entity;

import com.mecaps.posDev.Enums.ReturnReason;
import com.mecaps.posDev.Enums.ReturnStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ReturnOrderItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long return_order_item_id;

    @Column(nullable = false)
    private Double refund_amount;

    @Column(nullable = false)
    private Long return_quantity;

    @Enumerated(EnumType.STRING)
    private ReturnReason return_reason;

    @Enumerated(EnumType.STRING)
    private ReturnStatus return_status;

    @Column(nullable = false)
    private Double unit_price;

    @ManyToOne
    private Product product_id;

    @ManyToOne
    private ProductVariant product_variant_id;

    @ManyToOne
    private OrderItem order_item_id;

    @ManyToOne
    private Order order_id;


}
