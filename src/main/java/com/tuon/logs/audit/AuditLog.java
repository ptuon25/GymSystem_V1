package com.tuon.logs.audit;

import java.time.LocalDateTime;

public class AuditLog {

    Integer id;
    Integer employeeId;

    AuditAction action;
    EntityType entityType;

    Integer entityId;

    String description;

    Boolean success;

    LocalDateTime createdAt;

    public AuditLog(){

    }

    public AuditLog(Integer id, Integer employeeId, AuditAction action, EntityType entityType, Integer entityId, String description, Boolean success, LocalDateTime createdAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
        this.success = success;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", action=" + action +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", description='" + description + '\'' +
                ", success=" + success +
                ", createdAt=" + createdAt +
                '}';
    }
}
