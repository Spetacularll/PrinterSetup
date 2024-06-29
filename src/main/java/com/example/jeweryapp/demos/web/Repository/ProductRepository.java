package com.jewery.demo1.demos.web.Repository;

import com.jewery.demo1.demos.web.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
