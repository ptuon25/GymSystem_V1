package com.tuon.logs.audit;

import com.tuon.db.connection.DbConnection;
import com.tuon.exceptions.ServiceException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class AuditLogService {

    public void registerLog(AuditLog auditLog) {
        try {
            Connection conn = DbConnection.getConnection();
            AuditLogDAO auditLogDAO = new AuditLogDAOImpl(conn);
            validateForRegister(auditLog);
            auditLogDAO.insert(auditLog);
        } catch (ServiceException e) {
            System.err.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection();
        }
    }

    private void validateForRegister(AuditLog auditLog) {
        if (auditLog == null) {
            throw new ServiceException("AuditLog cannot be null");
        }
        if (auditLog.getAction() == null) {
            throw new ServiceException("Action cannot be null");
        }
        if (auditLog.getEntityType() == null) {
            throw new ServiceException("EntityType cannot be null");
        }
        if (auditLog.getDescription() == null || auditLog.getDescription().isBlank()) {
            throw new ServiceException("Description cannot be null or empty");
        }
    }

    public List<AuditLog> findAll() {
        try {
            Connection conn = DbConnection.getConnection();
            AuditLogDAO auditLogDAO = new AuditLogDAOImpl(conn);
            return auditLogDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return an empty list in case of an error
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<AuditLog> findByEmployee(Integer employeeId) {
        try {
            Connection conn = DbConnection.getConnection();
            AuditLogDAO auditLogDAO = new AuditLogDAOImpl(conn);
            return auditLogDAO.findByEmployee(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return an empty list in case of an error
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<AuditLog> findByEntityType(EntityType entityType) {
        try {
            Connection conn = DbConnection.getConnection();
            AuditLogDAO auditLogDAO = new AuditLogDAOImpl(conn);
            return auditLogDAO.findByEntityType(entityType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return an empty list in case of an error
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<AuditLog> findByAction(AuditAction action) {
        try {
            Connection conn = DbConnection.getConnection();
            AuditLogDAO auditLogDAO = new AuditLogDAOImpl(conn);
            return auditLogDAO.findByAction(action);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return an empty list in case of an error
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<AuditLog> findByDateRange(LocalDateTime start, LocalDateTime end) {
        try {
            Connection conn = DbConnection.getConnection();
            AuditLogDAO auditLogDAO = new AuditLogDAOImpl(conn);
            return auditLogDAO.findByDateRange(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return an empty list in case of an error
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<AuditLog> findByEmployeeAndAction(Integer employeeId, AuditAction action) {
        try {
            Connection conn = DbConnection.getConnection();
            AuditLogDAO auditLogDAO = new AuditLogDAOImpl(conn);
            return auditLogDAO.findByEmployeeAndAction(employeeId, action);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return an empty list in case of an error
        } finally {
            DbConnection.closeConnection();
        }
    }
}
