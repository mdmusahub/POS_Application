package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String name);

    @Query("""
    SELECT p
    FROM Product p
    JOIN p.product_variant v
    GROUP BY p
    ORDER BY MIN(v.productVariantPrice)
""")
    Page<Product> findAllByMinVariantPrice(Pageable pageable);

    @Query("""
    SELECT p
    FROM Product p
    JOIN p.product_variant v
    GROUP BY p
    ORDER BY MAX(v.productVariantPrice)
""")
    Page<Product> findAllByMaxVariantPrice(Pageable pageable);

}
