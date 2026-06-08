package com.tuon.services;

import com.tuon.db.DAO.EmployeeDAO;
import com.tuon.db.DAOImpl.EmployeeDAOImpl;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Employee;
import com.tuon.enums.EmployeeRole;
import com.tuon.services.passwords.AuthService;

import java.math.BigDecimal;
import java.sql.Connection;

public class AuthServiceTest {

    static void main() {
        System.out.println("========== AUTH SERVICE TEST ==========\n");
        try {
            Connection conn = DbConnection.getConnection();

            Employee employee = new Employee(null, "jdoe", null, "John Doe", EmployeeRole.MANAGER, BigDecimal.valueOf(5000));
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
            AuthService authService = new AuthService(employeeDAO);

            System.out.println("\nRegistering employee....");
            authService.registerEmployee(employee, "password123");
            System.out.println("Registered employee: " + employee);

            System.out.println("\nAuthenticating with correct password...");
            Employee authEmp = authService.authenticate("jdoe", "password123");
            System.out.println("Authenticated employee: " + authEmp);
            System.out.println("Password hash returned: " + authEmp.getPasswordHash());

            System.out.println("\nAuthenticating with wrong password...");
            try {
                authService.authenticate("jdoe", "wrongpassword");
                System.out.println("ERROR: Authentication should have failed but succeeded!");
            } catch (Exception e) {
                System.err.println("Expected authentication failure: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("\nAuthenticating non-existent user...");
            try {
                authService.authenticate("nonexistent", "password");
                System.out.println("ERROR: Authentication should have failed but succeeded!");
            } catch (Exception e) {
                System.err.println("Authentication failed as expected ✅");
            }

            System.out.println("\nChanging password....");
            authService.changePassword("jdoe", "password123", "newpassword456");
            System.out.println("Password changed.");
            System.out.println("Testing new password....");
            Employee employee2 = authService.authenticate("jdoe", "newpassword456");
            System.out.println("Authenticated employee: " + employee2);
            System.out.println("Testing old password....");
            try {
                authService.authenticate("jdoe", "password123");
                System.out.println("ERROR: Authentication should have failed but succeeded!");
            } catch (Exception e) {
                System.err.println("Authentication failed as expected ✅");
            }


            employeeDAO.deleteById(employee.getId());
            System.out.println("\n========== AUTH SERVICE TEST COMPLETED ==========\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DbConnection.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Successfully test
    }
}
