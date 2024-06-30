package com.example.jeweryapp.demos.web.Repository;

import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InboundRecordRepository extends
        JpaRepository<InboundRecord,Long> {
    List<InboundRecord> findByProduct(Product product);

}
