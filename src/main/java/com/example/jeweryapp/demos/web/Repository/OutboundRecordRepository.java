package com.example.jeweryapp.demos.web.Repository;

import com.example.jeweryapp.demos.web.Entity.OutboundRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutboundRecordRepository extends JpaRepository<OutboundRecord,Long> {
}
