package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Response.OrderItemResponse;
import com.mecaps.posDev.Service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @DeleteMapping("/{orderItemId}")
    public String deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return "Order item deleted successfully";
    }

    @GetMapping("/order/{orderId}")
    public List<OrderItemResponse> getOrderItemsByOrder(@PathVariable Long orderId) {
        return orderItemService.getOrderItemsByOrder(orderId);
    }
}
