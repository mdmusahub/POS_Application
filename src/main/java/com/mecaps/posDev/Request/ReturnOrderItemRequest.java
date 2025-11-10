package com.mecaps.posDev.Request;

import com.mecaps.posDev.Entity.Order;
import com.mecaps.posDev.Entity.OrderItem;
import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductVariant;
import com.mecaps.posDev.Enums.ReturnReason;
import com.mecaps.posDev.Enums.ReturnStatus;
import lombok.Data;

@Data
public class ReturnOrderItemRequest
{
    private Long order_item_id;
    private Long return_quantity;
    private ReturnReason return_reason;
    private ReturnStatus return_status;


}
