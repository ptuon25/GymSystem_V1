package com.tuon.logs.audit;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogDAO {

    void insert(AuditLog auditLog);

    List<AuditLog> findAll();

    List<AuditLog> findByEmployee(Integer employeeId);

    List<AuditLog> findByAction(AuditAction action);

    List<AuditLog> findByEntityType(EntityType entityType);

    List<AuditLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<AuditLog> findByEmployeeAndAction(Integer employeeId, AuditAction action);

}
