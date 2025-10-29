package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
=======
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory ,Long> {
>>>>>>> 0c32961e4a3c0849b0845da4eccc22dd66fc3a0c
}
