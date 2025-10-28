package com.mecaps.posDev.Repository;

import com.mecaps.posDev.Entity.Category;
import com.mecaps.posDev.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
