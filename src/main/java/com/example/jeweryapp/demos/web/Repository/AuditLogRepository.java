package com.example.jeweryapp.demos.web.Repository;

import com.example.jeweryapp.demos.web.Entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

}
