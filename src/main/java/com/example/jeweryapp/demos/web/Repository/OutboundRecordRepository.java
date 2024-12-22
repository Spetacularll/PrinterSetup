package com.example.jeweryapp.demos.web.Repository;

import com.example.jeweryapp.demos.web.Entity.OutboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.common.OutRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OutboundRecordRepository extends JpaRepository<OutboundRecord,Long> {
    List<OutboundRecord> findByProduct(Product product);
    @Query("SELECT new com.example.jeweryapp.demos.web.common.OutRecord(o.destination, p.productName, o.outboundDate, p.price,p.barcode) " +
            "FROM OutboundRecord o " +
            "JOIN o.product p")
    List<OutRecord> findAllOutboundRecords();
}
