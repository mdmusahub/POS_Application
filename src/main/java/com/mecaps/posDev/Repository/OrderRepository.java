package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.Customer;
import com.mecaps.posDev.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);

}
