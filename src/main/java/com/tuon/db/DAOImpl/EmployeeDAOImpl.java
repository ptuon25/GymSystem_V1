package com.tuon.db.DAOImpl;

import com.tuon.db.DAO.EmployeeDAO;
import com.tuon.exceptions.DbException;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Employee;
import com.tuon.enums.EmployeeRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    private final Connection conn;

    public EmployeeDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Employee employee) {
        String sql1 = "INSERT INTO employee (username, password_hash, name, role, salary) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, employee.getUsername());
            st.setString(2, employee.getPasswordHash());
            st.setString(3, employee.getName());
            st.setString(4, employee.getRole().name());
            st.setObject(5, employee.getSalary(), Types.DOUBLE);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    employee.setId(rs.getInt(1));
                } else {
                    // Fallback: use LAST_INSERT_ID() on same connection
                    try (PreparedStatement st2 = conn.prepareStatement("SELECT LAST_INSERT_ID()")) {
                        try (ResultSet rs2 = st2.executeQuery()) {
                            if (rs2.next()) {
                                employee.setId(rs2.getInt(1));
                            }
                        }
                    }
                }
            } else {
                throw new DbException("Unexpected error! No rows affected when inserting employee.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void update(Employee employee) {
        String sql2 = "UPDATE employee SET username = ?, password_hash = ?, name = ?, role = ?, salary = ? WHERE id = ?";
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql2);
            st.setString(1, employee.getUsername());
            st.setString(2, employee.getPasswordHash());
            st.setString(3, employee.getName());
            st.setString(4, employee.getRole().name());
            st.setObject(5, employee.getSalary(), Types.DOUBLE);
            st.setInt(6, employee.getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("No employee found with the provided ID.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql3 = "DELETE FROM employee WHERE id = ?";
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql3);
            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("No employee found with the provided ID.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public Employee findById(Integer id) {
        String sql4 = "SELECT id, username, password_hash, name, role, salary FROM employee WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql4);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateEmployee(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public List<Employee> findAll() {
        String sql5 = "SELECT id, username, password_hash, name, role, salary FROM employee ORDER BY name";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql5);
            rs = st.executeQuery();

            List<Employee> list = new ArrayList<>();

            while (rs.next()) {
                list.add(instantiateEmployee(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    /**
     * Método para autenticação: busca funcionário por username
     */
    public Employee findByUsername(String username) {
        String sql6 = "SELECT id, username, password_hash, name, role, salary FROM employee WHERE username = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql6);
            st.setString(1, username);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateEmployee(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    private Employee instantiateEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("id"));
        emp.setUsername(rs.getString("username"));
        emp.setPasswordHash(rs.getString("password_hash"));
        emp.setName(rs.getString("name"));
        emp.setRole(EmployeeRole.valueOf(rs.getString("role")));
        emp.setSalary(rs.getObject("salary", Double.class));
        return emp;
    }
}