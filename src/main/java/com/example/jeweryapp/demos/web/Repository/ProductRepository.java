package com.example.jeweryapp.demos.web.Repository;

import com.example.jeweryapp.demos.web.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByBarcode(String barcode);
    List<Product> findByStock(int stock);
}
