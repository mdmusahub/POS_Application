package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.OrderItem;
import com.mecaps.posDev.Entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    Optional<OrderItem> findByProductVariant(ProductVariant productVariant);
}
