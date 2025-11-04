package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Long , Customer> {
    Optional<Customer> customer_phone_number();
}
