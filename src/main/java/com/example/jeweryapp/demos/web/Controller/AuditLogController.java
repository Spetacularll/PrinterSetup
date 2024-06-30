package com.example.jeweryapp.demos.web.Controller;

import com.example.jeweryapp.demos.web.Entity.AuditLog;
import com.example.jeweryapp.demos.web.Service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/api/audit-logs")
    public List<AuditLog> getAuditLogs(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                       @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return auditLogService.getAuditLogs(start, end);
    }
}