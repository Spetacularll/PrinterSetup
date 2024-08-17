package com.example.jeweryapp.demos.web.Repository;

import com.example.jeweryapp.demos.web.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByBarcode(String barcode);

    @Query("SELECT p FROM Product p WHERE p.stock = :stock AND p.isDeleted = false")
    List<Product> findByStock(@Param("stock")int stock);

    Product findProductByBarcode(String barcode);
}
