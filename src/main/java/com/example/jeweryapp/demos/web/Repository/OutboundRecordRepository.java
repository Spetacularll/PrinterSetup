package com.example.jeweryapp.demos.web.Repository;

import com.example.jeweryapp.demos.web.Entity.OutboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OutboundRecordRepository extends JpaRepository<OutboundRecord,Long> {
    List<OutboundRecord> findByProduct(Product product);
}
