package com.jewery.demo1.demos.web.Repository;

import com.jewery.demo1.demos.web.Entity.OutboundRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboundRecordRepository extends JpaRepository<OutboundRecord,Long> {
}
