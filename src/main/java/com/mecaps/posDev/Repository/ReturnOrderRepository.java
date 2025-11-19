package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.Order;
import com.mecaps.posDev.Entity.ReturnOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReturnOrderRepository extends JpaRepository<ReturnOrder, Long> {
    Optional<ReturnOrder> findReturnOrderByOrder (Order order);

}
