package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    Optional<ProductVariant> findByVariantName(String name);
    List<ProductVariant> findByProductId_ProductId(Long productId) ;
}
