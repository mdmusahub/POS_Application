package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.ProductInventory;
import com.mecaps.posDev.Entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory ,Long> {
    Optional<ProductInventory> findByProductVariant_productVariantId(Long productVariantId);
}
