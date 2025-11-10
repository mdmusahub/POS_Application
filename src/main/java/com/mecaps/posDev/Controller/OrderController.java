package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.OrderRequest;
import com.mecaps.posDev.Response.OrderResponse;
import com.mecaps.posDev.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/all")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAll();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@PathVariable Long orderId) {
        return orderService.getById(orderId);
    }

    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        return orderService.deleteOrder(orderId);
    }
}
