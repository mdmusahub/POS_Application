package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory ,Long> {
    Optional<ProductInventory> findById(Long aLong);
}
