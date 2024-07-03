package com.example.jeweryapp.demos.web.Service;

import com.example.jeweryapp.demos.web.Entity.AuditLog;
import com.example.jeweryapp.demos.web.Repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private ObjectMapper objectMapper;
    public String serializeEntity(Object entity) throws Exception {
        return objectMapper.writeValueAsString(entity);
    }

    public List<AuditLog> getAuditLogs(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findAllByTimestampBetween(start, end);
    }
}

