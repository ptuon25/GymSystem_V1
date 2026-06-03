package com.tuon.logs.audit;

import com.tuon.db.connection.DbConnection;
import com.tuon.exceptions.DbException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class AuditLogDAOImpl implements AuditLogDAO {

    private final Connection conn;

    public AuditLogDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(AuditLog auditLog) {
        String sql1 = "INSERT INTO audit_log (employee_id, action, entity_type, entity_id, description, success) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, auditLog.getEmployeeId());
            st.setString(2, auditLog.getAction().name());
            st.setString(3, auditLog.getEntityType().name());
            st.setInt(4, auditLog.getEntityId());
            st.setString(5, auditLog.getDescription());
            st.setBoolean(6, auditLog.getSuccess());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    auditLog.setId(id);
                } else {
                    throw new DbException("Unexpected error! Error retrieving generated ID.");
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }


    @Override
    public List<AuditLog> findAll() {
        String sql2 = "SELECT * FROM audit_log ORDER BY created_at DESC";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql2);
            rs = st.executeQuery();
            List<AuditLog> auditLogs = new java.util.ArrayList<>();
            while (rs.next()) {
                AuditLog auditLog = instantiateAuditLog(rs);
                auditLogs.add(auditLog);
            }
            return auditLogs;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public List<AuditLog> findByEmployee(Integer employeeId) {
        String sql3 = "SELECT * FROM audit_log WHERE employee_id = ? ORDER BY created_at DESC";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql3);
            st.setInt(1, employeeId);
            rs = st.executeQuery();
            List<AuditLog> auditLogs = new java.util.ArrayList<>();
            while (rs.next()) {
                AuditLog auditLog = instantiateAuditLog(rs);
                auditLogs.add(auditLog);
            }
            return auditLogs;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public List<AuditLog> findByAction(AuditAction action) {
        String sql4 = "SELECT * FROM audit_log WHERE action = ? ORDER BY created_at DESC";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql4);
            st.setString(1, action.name());
            rs = st.executeQuery();
            List<AuditLog> auditLogs = new java.util.ArrayList<>();
            while (rs.next()) {
                AuditLog auditLog = instantiateAuditLog(rs);
                auditLogs.add(auditLog);
            }
            return auditLogs;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public List<AuditLog> findByEntityType(EntityType entityType) {
        String sql5 = "SELECT * FROM audit_log WHERE entity_type = ? ORDER BY created_at DESC";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql5);
            st.setString(1, entityType.name());
            rs = st.executeQuery();
            List<AuditLog> auditLogs = new java.util.ArrayList<>();
            while (rs.next()) {
                AuditLog auditLog = instantiateAuditLog(rs);
                auditLogs.add(auditLog);
            }
            return auditLogs;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public List<AuditLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql6 = "SELECT * FROM audit_log WHERE created_at BETWEEN ? AND ? ORDER BY created_at DESC";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql6);
            st.setTimestamp(1, java.sql.Timestamp.valueOf(startDate));
            st.setTimestamp(2, java.sql.Timestamp.valueOf(endDate));
            rs = st.executeQuery();
            List<AuditLog> auditLogs = new java.util.ArrayList<>();
            while (rs.next()) {
                AuditLog auditLog = instantiateAuditLog(rs);
                auditLogs.add(auditLog);
            }
            return auditLogs;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public List<AuditLog> findByEmployeeAndAction(Integer employeeId, AuditAction action) {
        String sql7 = "SELECT * FROM audit_log WHERE employee_id = ? AND action = ? ORDER BY created_at DESC";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql7);
            st.setInt(1, employeeId);
            st.setString(2, action.name());
            rs = st.executeQuery();
            List<AuditLog> auditLogs = new java.util.ArrayList<>();
            while (rs.next()) {
                AuditLog auditLog = instantiateAuditLog(rs);
                auditLogs.add(auditLog);
            }
            return auditLogs;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    private AuditLog instantiateAuditLog(ResultSet rs) throws SQLException {
        AuditLog auditLog = new AuditLog();
        auditLog.setId(rs.getInt("id"));
        auditLog.setEmployeeId(rs.getObject("employee_id", Integer.class));
        auditLog.setAction(AuditAction.valueOf(rs.getString("action")));
        auditLog.setEntityType(EntityType.valueOf(rs.getString("entity_type")));
        auditLog.setEntityId(rs.getObject("entity_id", Integer.class));
        auditLog.setDescription(rs.getString("description"));
        auditLog.setSuccess(rs.getBoolean("success"));
        auditLog.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return auditLog;
    }
}