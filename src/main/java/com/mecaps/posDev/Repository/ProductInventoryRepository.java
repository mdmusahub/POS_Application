package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
}
