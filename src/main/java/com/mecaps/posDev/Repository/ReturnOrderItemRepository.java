package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.Order;
import com.mecaps.posDev.Entity.ReturnOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnOrderItemRepository extends JpaRepository<ReturnOrderItem, Long> {
    List<ReturnOrderItem> findAllReturnOrderItemsByOrder(Order order);
}
