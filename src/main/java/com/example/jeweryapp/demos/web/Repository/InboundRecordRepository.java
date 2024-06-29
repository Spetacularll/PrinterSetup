package com.jewery.demo1.demos.web.Repository;

import com.jewery.demo1.demos.web.Entity.InboundRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundRecordRepository extends
        JpaRepository<InboundRecord,Long> {
}
