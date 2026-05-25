package com.tuon.services;

import com.tuon.db.DbException;
import com.tuon.db.EmployeeDAO;
import com.tuon.db.EmployeeDAOImpl;
import com.tuon.entities.Employee;

import java.util.List;

public class EmployeeService {

    private EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public Employee createEmployee(Employee employee, String plainPassword) {
        validateEmployeeForCreate(employee);
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new DbException("Password is required");
        }

        String hash = PasswordUtil.hashPassword(plainPassword);
        employee.setPasswordHash(hash);

        employeeDAO.insert(employee);

        employee.setPasswordHash(null);
        return employee;
    }

    private void validateEmployeeForCreate(Employee employee) {
        if (employee == null) {
            throw new DbException("Employee cannot be null");
        }
        if (employee.getId() != null) {
            throw new DbException("Employee id must be null");
        }
        if (employee.getUsername() == null || employee.getUsername().isEmpty()) {
            throw new DbException("Employee username cannot be null or empty");
        }
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new DbException("Employee name cannot be null or empty");
        }
        if (employee.getRole() == null) {
            throw new DbException("Employee role cannot be null");
        }
        if (employee.getSalary() == null || employee.getSalary() == 0.0) {
            throw new DbException("Employee salary cannot be null or empty");
        }
    }

    public Employee updateEmployee(Employee employee) {
        validateEmployeeForUpdate(employee);

        // preservar hash caso seja null no objeto passado
        if (employee.getPasswordHash() == null) {
            Employee persisted = employeeDAO.findById(employee.getId());
            if (persisted == null) throw new DbException("Employee not found");
            employee.setPasswordHash(persisted.getPasswordHash());
        }

        employeeDAO.update(employee);

        Employee updated = employeeDAO.findById(employee.getId());
        // não retornar passwordHash
        if (updated != null) updated.setPasswordHash(null);
        return updated;
    }

    private void validateEmployeeForUpdate(Employee employee) {
        if (employee == null) throw new DbException("Employee cannot be null");
        if (employee.getId() == null || employee.getId() <= 0) throw new DbException("Employee id required for update");
        if (employee.getUsername() == null || employee.getUsername().trim().isEmpty())
            throw new DbException("Username required");
        if (employee.getName() == null || employee.getName().trim().isEmpty()) throw new DbException("Name required");
        if (employee.getRole() == null) throw new DbException("Role required");
        if (employee.getSalary() == null || employee.getSalary() <= 0) throw new DbException("Salary must be > 0");
    }

    public void changePassword(Integer employeeId, String oldPlainPassword, String newPlainPassword) {
        if (employeeId == null || employeeId <= 0) throw new DbException("Employee id is required");
        if (oldPlainPassword == null || newPlainPassword == null) throw new DbException("Passwords are required");

        Employee persisted = employeeDAO.findById(employeeId);
        if (persisted == null) throw new DbException("Employee not found");

        if (!PasswordUtil.verifyPassword(oldPlainPassword, persisted.getPasswordHash())) {
            throw new DbException("Invalid current password");
        }

        String newHash = PasswordUtil.hashPassword(newPlainPassword);
        persisted.setPasswordHash(newHash);
        employeeDAO.update(persisted);
    }

    public void deleteEmployee(Integer id) {
        if (id == null || id <= 0) throw new DbException("Employee id is required");
        employeeDAO.deleteById(id);
    }

    public Employee findById(Integer id) {
        if (id == null || id <= 0) throw new DbException("Employee id is required");
        Employee e = employeeDAO.findById(id);
        if (e != null) e.setPasswordHash(null);
        return e;
    }

    public Employee findByUsername(String username) {
        if (username == null || username.isBlank()) throw new DbException("Username is required");
        Employee e = employeeDAO.findByUsername(username);
        if (e != null) e.setPasswordHash(null);
        return e;
    }

    public List<Employee> findAll() {
        List<Employee> list = employeeDAO.findAll();
        if (list != null) {
            for (Employee e : list) {
                if (e != null) e.setPasswordHash(null);
            }
        }
        return list;
    }
}

