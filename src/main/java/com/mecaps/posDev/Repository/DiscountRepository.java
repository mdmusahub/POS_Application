package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Long> {

    Optional<Discount> findByproductVariant(Long variantId);
}
