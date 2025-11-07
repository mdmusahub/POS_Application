package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.OrderItem;
import com.mecaps.posDev.Response.OrderItemResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrder_Order_id(Long orderId);

}
