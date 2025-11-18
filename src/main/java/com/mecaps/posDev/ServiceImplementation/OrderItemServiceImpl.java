package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.OrderItem;
import com.mecaps.posDev.Exception.ResourceNotFoundException;
import com.mecaps.posDev.Repository.OrderItemRepository;
import com.mecaps.posDev.Repository.OrderRepository;
import com.mecaps.posDev.Repository.ProductRepository;
import com.mecaps.posDev.Repository.ProductVariantRepository;
import com.mecaps.posDev.Response.OrderItemResponse;
import com.mecaps.posDev.Service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Override
    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderItem not found with ID: " + orderItemId
                ));

        orderItemRepository.delete(orderItem);
    }

   @Override
    public List<OrderItemResponse> getOrderItemsByOrder(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder_orderId(orderId);

        if (orderItems.isEmpty()) {
            throw new ResourceNotFoundException("No order items found for Order ID: " + orderId);
        }

        return orderItems.stream().map(OrderItemResponse ::new).toList();
    }

}
