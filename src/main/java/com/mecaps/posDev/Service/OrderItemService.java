package com.mecaps.posDev.Service;

import com.mecaps.posDev.Response.OrderItemResponse;

import java.util.List;

public interface OrderItemService {
    void deleteOrderItem(Long orderItemId);
    List<OrderItemResponse> getOrderItemsByOrder(Long orderId);
}
