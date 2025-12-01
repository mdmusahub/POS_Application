package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.Order;
import com.mecaps.posDev.Entity.OrderItem;
import com.mecaps.posDev.Entity.ReturnOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReturnOrderItemRepository extends JpaRepository<ReturnOrderItem, Long> {
    List<ReturnOrderItem> findAllReturnOrderItemsByOrderId(Order order);
    @Query("""
       SELECT COALESCE(SUM(r.returnQuantity), 0)
       FROM ReturnOrderItem r
       WHERE r.orderItemId.order_item_id = :orderItemId
       """)
    Long sumReturnQty(@Param("orderItemId") Long orderItemId);

    boolean existsByOrderItemId(OrderItem oldItem);
}
