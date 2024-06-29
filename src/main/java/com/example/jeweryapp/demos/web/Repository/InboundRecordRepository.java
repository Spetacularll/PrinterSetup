package com.example.jeweryapp.demos.web.Repository;

import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InboundRecordRepository extends
        JpaRepository<InboundRecord,Long> {
}
