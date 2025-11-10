package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.OrderRequest;
import com.mecaps.posDev.Response.OrderResponse;

import java.util.List;

public interface OrderService {
    String createOrder(OrderRequest orderRequest);
    List<OrderResponse> getAll();
    OrderResponse getById(Long orderId);
    String deleteOrder(Long order_id);
    OrderResponse updateOrder(Long id, OrderRequest orderRequest);
}
