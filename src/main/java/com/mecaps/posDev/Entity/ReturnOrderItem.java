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
    private Long returnOrderItemId;

    @Column(nullable = false)
    private Double refundAmount;

    @Column(nullable = false)
    private Long returnQuantity;

    @Enumerated(EnumType.STRING)
    private ReturnReason returnReason;

    @Enumerated(EnumType.STRING)
    private ReturnStatus returnStatus;

    @Column(nullable = false)
    private Double unitPrice;

    @ManyToOne
    private Product productId;

    @ManyToOne
    private ProductVariant productVariantId;

    @ManyToOne
    private OrderItem orderItemId;

    @ManyToOne
    private Order orderId;


}
