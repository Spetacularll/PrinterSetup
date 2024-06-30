package com.example.jeweryapp.demos.web.Service;

import com.example.jeweryapp.demos.web.Entity.AuditLog;
import com.example.jeweryapp.demos.web.Repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public List<AuditLog> getAuditLogs(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findAllByTimestampBetween(start, end);
    }
}

