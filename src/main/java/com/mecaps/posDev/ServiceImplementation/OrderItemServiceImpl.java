package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.OrderItem;
import com.mecaps.posDev.Exception.ResourceNotFoundException;
import com.mecaps.posDev.Repository.OrderItemRepository;
import com.mecaps.posDev.Response.OrderItemResponse;
import com.mecaps.posDev.Service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing order items.
 * <p>
 * Provides operations for deleting order items and retrieving all items
 * associated with a specific order.
 */
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    /**
     * Deletes an order item using its ID.
     *
     * @param orderItemId the ID of the order item to delete
     * @throws ResourceNotFoundException if the order item does not exist
     */
    @Override
    public void deleteOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderItem not found with ID: " + orderItemId
                ));

        orderItemRepository.delete(orderItem);
    }

    /**
     * Retrieves all order items belonging to a specific order.
     *
     * @param orderId the ID of the order whose items need to be fetched
     * @return list of OrderItemResponse containing item details
     * @throws ResourceNotFoundException if no items exist for the given order
     */
    @Override
    public List<OrderItemResponse> getOrderItemsByOrder(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder_orderId(orderId);

        if (orderItems.isEmpty()) {
            throw new ResourceNotFoundException("No order items found for Order ID: " + orderId);
        }

        return orderItems.stream().map(OrderItemResponse::new).toList();
    }
}
