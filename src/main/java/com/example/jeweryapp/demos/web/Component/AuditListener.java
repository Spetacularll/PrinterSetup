package com.example.jeweryapp.demos.web.Component;

import com.example.jeweryapp.demos.web.Entity.AuditLog;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Repository.AuditLogRepository;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class AuditListener {

    private static AuditLogRepository auditLogRepository;

    @Autowired
    public void setAuditLogRepository(AuditLogRepository auditLogRepository) {
        AuditListener.auditLogRepository = auditLogRepository;
    }

    @PrePersist
    public void prePersist(Object entity) {
        saveAuditLog("INSERT", entity, null);
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        saveAuditLog("UPDATE", entity, getOldData(entity));
    }

    @PreRemove
    public void preRemove(Object entity) {
        saveAuditLog("DELETE", entity, getOldData(entity));
    }

    private void saveAuditLog(String operationType, Object entity, String oldData) {
        try {
            String entityName = entity.getClass().getSimpleName();
            Long entityId = (Long) entity.getClass().getMethod("getId").invoke(entity);
            String newData = new ObjectMapper().writeValueAsString(entity);

            AuditLog auditLog = new AuditLog(operationType, entityName, entityId, oldData, newData);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOldData(Object entity) {
        try {
            String entityName = entity.getClass().getSimpleName();
            Long entityId = (Long) entity.getClass().getMethod("getId").invoke(entity);

            // Assume a generic findById method for simplicity, you might need to customize this.
            Object oldEntity = auditLogRepository.findById(entityId).orElse(null);
            if (oldEntity != null) {
                return new ObjectMapper().writeValueAsString(oldEntity);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
