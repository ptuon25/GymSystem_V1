package com.tuon.services;

import com.tuon.db.DAO.EmployeeDAO;
import com.tuon.db.DAOImpl.EmployeeDAOImpl;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Employee;
import com.tuon.enums.EmployeeRole;
import com.tuon.exceptions.ServiceException;
import com.tuon.util.PasswordUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class EmployeeService {

    public Employee createEmployee(Employee employee, String plainPassword) {
        try {
            Connection conn = DbConnection.getConnection();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            validateEmployeeForCreate(employee);
            if (plainPassword == null || plainPassword.isBlank()) {
                throw new ServiceException("Password is required");
            }

            String hash = PasswordUtil.hashPassword(plainPassword);
            employee.setPasswordHash(hash);

            employeeDAO.insert(employee);

            employee.setPasswordHash(null);
            return employee;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    private void validateEmployeeForCreate(Employee employee) {
        if (employee == null) {
            throw new ServiceException("Employee cannot be null");
        }
        if (employee.getId() != null) {
            throw new ServiceException("Employee id must be null");
        }
        if (employee.getUsername() == null || employee.getUsername().isEmpty()) {
            throw new ServiceException("Employee username cannot be null or empty");
        }
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new ServiceException("Employee name cannot be null or empty");
        }
        if (employee.getRole() == null) {
            throw new ServiceException("Employee role cannot be null");
        }
        if (employee.getSalary() == null || employee.getSalary().compareTo(BigDecimal.ZERO) == 0) {
            throw new ServiceException("Employee salary cannot be null or empty");
        }
    }

    public Employee updateEmployee(Employee employee) {
        try {
            Connection conn = DbConnection.getConnection();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            validateEmployeeForUpdate(employee);

            // preservar hash caso seja null no objeto passado
            if (employee.getPasswordHash() == null) {
                Employee persisted = employeeDAO.findById(employee.getId());
                if (persisted == null) throw new ServiceException("Employee not found");
                employee.setPasswordHash(persisted.getPasswordHash());
            }

            employeeDAO.update(employee);

            Employee updated = employeeDAO.findById(employee.getId());
            // não retornar passwordHash
            if (updated != null) updated.setPasswordHash(null);
            return updated;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    private void validateEmployeeForUpdate(Employee employee) {
        if (employee == null) throw new ServiceException("Employee cannot be null");
        if (employee.getId() == null || employee.getId() <= 0)
            throw new ServiceException("Employee id required for update");
        if (employee.getUsername() == null || employee.getUsername().trim().isEmpty())
            throw new ServiceException("Username required");
        if (employee.getName() == null || employee.getName().trim().isEmpty())
            throw new ServiceException("Name required");
        if (employee.getRole() == null) throw new ServiceException("Role required");
        if (employee.getSalary() == null || employee.getSalary().compareTo(BigDecimal.ZERO) <= 0) throw new ServiceException("Salary must be > 0");
    }

    public void changePassword(Integer employeeId, String oldPlainPassword, String newPlainPassword) {
        try {
            Connection conn = DbConnection.getConnection();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            if (employeeId == null || employeeId <= 0) throw new ServiceException("Employee id is required");
            if (oldPlainPassword == null || newPlainPassword == null)
                throw new ServiceException("Passwords are required");

            Employee persisted = employeeDAO.findById(employeeId);
            if (persisted == null) throw new ServiceException("Employee not found");

            if (!PasswordUtil.verifyPassword(oldPlainPassword, persisted.getPasswordHash())) {
                throw new ServiceException("Invalid current password");
            }

            String newHash = PasswordUtil.hashPassword(newPlainPassword);
            persisted.setPasswordHash(newHash);
            employeeDAO.update(persisted);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public void deleteEmployee(Integer id) {
        try {
            Connection conn = DbConnection.getConnection();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            if (id == null || id <= 0) throw new ServiceException("Employee id is required");
            employeeDAO.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public Employee findById(Integer id) {
        try {
            Connection conn = DbConnection.getConnection();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            if (id == null || id <= 0) throw new ServiceException("Employee id is required");
            Employee e = employeeDAO.findById(id);
            if (e != null) e.setPasswordHash(null);
            return e;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public Employee findByUsername(String username) {
        try {
            Connection conn = DbConnection.getConnection();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            if (username == null || username.isBlank()) throw new ServiceException("Username is required");
            Employee e = employeeDAO.findByUsername(username);
            if (e != null) e.setPasswordHash(null);
            return e;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<Employee> findAll() {
        try {
            Connection conn = DbConnection.getConnection();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            List<Employee> list = employeeDAO.findAll();
            if (list != null) {
                for (Employee e : list) {
                    if (e != null) e.setPasswordHash(null);
                }
            }
            return list;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<Employee> findInvalidEmployees() {
        try {
            Connection conn = DbConnection.getConnection();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            return employeeDAO.findAll().stream()
                    .filter(e -> e.getSalary() == null || e.getSalary().compareTo(BigDecimal.ZERO) <= 0 || e.getRole() == null)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public void applySalaryAdjustment(Employee e) {
        try {
            Connection conn = DbConnection.getConnection();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            if (e.getRole().equals(EmployeeRole.RECEPTIONIST)) {
                e.setSalary(e.getSalary().compareTo(BigDecimal.ZERO) > 0 ? e.getSalary().multiply(BigDecimal.valueOf(1.10)) : BigDecimal.ZERO);
                // 10% de aumento para recepcionistas
            } else if (e.getRole().equals(EmployeeRole.TRAINER)) {
                e.setSalary(e.getSalary().compareTo(BigDecimal.ZERO) > 0 ? e.getSalary().multiply(BigDecimal.valueOf(1.15)) : BigDecimal.ZERO); // 15% de aumento para treinadores
            } else if (e.getRole().equals(EmployeeRole.MANAGER)) {
                e.setSalary(e.getSalary().compareTo(BigDecimal.ZERO) > 0 ? e.getSalary().multiply(BigDecimal.valueOf(1.20)) : BigDecimal.ZERO); // 20% de aumento para gerentes
            }
            employeeDAO.update(e);
        } catch (Exception exception) {
            throw new ServiceException(exception.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }
}

