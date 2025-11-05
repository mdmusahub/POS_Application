package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.GstTax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GstTaxRepository extends JpaRepository<GstTax,Long> {
}
