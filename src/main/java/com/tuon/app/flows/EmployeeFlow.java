package com.tuon.app.flows;

import com.tuon.app.auth.AuthorizationManager;
import com.tuon.app.auth.LoginManager;
import com.tuon.app.auth.SessionManager;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Employee;
import com.tuon.enums.EmployeeRole;
import com.tuon.exceptions.FlowExecption;
import com.tuon.services.EmployeeService;

import java.math.BigDecimal;
import java.util.Scanner;

public class EmployeeFlow {

    private final Scanner sc;
    private final EmployeeService employeeService;
    private final SessionManager sessionManager = new SessionManager();
    private final LoginManager loginManager = new LoginManager(sessionManager);
    private final AuthorizationManager authorizationManager = new AuthorizationManager(sessionManager);

    private String currentToken = null;

    public EmployeeFlow() {
        this.sc = new Scanner(System.in);
        this.employeeService = new EmployeeService();
    }

    public void loginFlow() {
        String username = readString("Username: ");
        String password;
        // Preferir java.io.Console para não mostrar senha (quando disponível)
        java.io.Console console = System.console();
        if (console != null) {
            password = new String(console.readPassword("Password: "));
        } else {
            password = readString("Password: ");
        }
        try {
            currentToken = loginManager.login(username, password);
            System.out.println("Login successful. Token: " + currentToken);
        } catch (FlowExecption e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    public void logoutFlow() {
        if (currentToken != null) {
            loginManager.logout(currentToken);
            currentToken = null;
            System.out.println("Logged out.");
        }
    }


    public void createEmployee() {
        try {
            authorizationManager.requireRole(currentToken, EmployeeRole.MANAGER);

            System.out.println("Enter the information for registration:");
            String name = readString("Enter the name: ");
            String username = readString("Enter the username: ");
            String roleInput = readString("Enter the employee role (TRAINER/RECEPTIONIST/MANAGER): ");
            EmployeeRole empRole;
            try {
                empRole = EmployeeRole.valueOf(roleInput.trim().toUpperCase());
            } catch (IllegalArgumentException iae) {
                System.out.println("Invalid role. Use one of: TRAINER, RECEPTIONIST, MANAGER");
                return;
            }
            BigDecimal salary = readBigDecimal("Enter the salary: ");
            String plainPassword;
            java.io.Console console = System.console();
            if (console != null) {
                plainPassword = new String(console.readPassword("Password: "));
            } else {
                plainPassword = readString("Enter the initial password: ");
            }
            Employee employee = new Employee(null, username, null, name, empRole, salary);
            Employee createdEmp = employeeService.createEmployee(employee, plainPassword);
            System.out.println("Employee created successfully.");
            System.out.println(createdEmp);
            System.out.println(employee.getCreatedAt());
        } catch (FlowExecption e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateEmployee() {
        try {
            authorizationManager.requireRole(currentToken, EmployeeRole.MANAGER);
            Integer getId = readInt("Enter the employee ID to update: ");
            Employee employee = findeEmployeeOrNull(getId);
            if (employee == null) {
                return;
            }
            Employee originalEmployee = new Employee(employee.getId(), employee.getUsername(), null, employee.getName(), employee.getRole(), employee.getSalary());
            System.out.println("Current employee data: ");
            System.out.println(employee);

            boolean editing = true;
            boolean hasChanges = false;
            while (editing) {
                int choice = readInt("\nWhat would you like to update? \\n1.name \\n2. username \\n3. role \\n4. salary \\n0. exit\"");
                switch (choice) {
                    case 1:
                        System.out.println("Current name: " + employee.getName());
                        String newName = readString("Enter the new name: ");
                        if (employee.getName().equals(newName)) {
                            System.out.println("The new name is already in use.");
                            break;
                        }
                        String confirmName = readString("Do you want to change the current name " + employee.getName() + " to" + newName + "? (yes/no)");
                        if (!confirmName.equalsIgnoreCase("yes")) {
                            System.out.println("Update cancelled.");
                            return;
                        }
                        employee.setName(newName);
                        hasChanges = true;
                        System.out.println("Updated successfully.");
                        System.out.println(employee);
                        break;
                    case 2:
                        System.out.println("Current username: " + employee.getUsername());
                        String newUsername = readString("Enter the new name: ");
                        if (employee.getUsername().equals(newUsername)) {
                            System.out.println("The new username is already in use.");
                            break;
                        }
                        String confirmUsername = readString("Do you want to change the current name " + employee.getUsername() + " to" + newUsername + "? (yes/no)");
                        if (!confirmUsername.equalsIgnoreCase("yes")) {
                            System.out.println("Update cancelled.");
                            return;
                        }
                        employee.setUsername(newUsername);
                        hasChanges = true;
                        System.out.println("Updated successfully.");
                        System.out.println(employee);
                        break;
                    case 3:
                        System.out.println("Current role: " + employee.getRole());
                        String newRole = readString("Enter the new role: ");
                        String confirmRole = readString("Do you want to change the current role " + employee.getRole() + " to" + newRole + "? (yes/no)");
                        if (!confirmRole.equalsIgnoreCase("yes")) {
                            System.out.println("Update cancelled.");
                            return;
                        }
                        try {
                            EmployeeRole parsedRole = EmployeeRole.valueOf(newRole.trim().toUpperCase());
                            if (employee.getRole().equals(parsedRole)) {
                                System.out.println("The new role is the same as current role.");
                                break;
                            }
                            employee.setRole(parsedRole);
                            hasChanges = true;
                            System.out.println("Updated successfully.");
                            System.out.println(employee);
                        } catch (IllegalArgumentException iae) {
                            System.out.println("Invalid role. Use one of: TRAINER, RECEPTIONIST, MANAGER");
                        }
                        break;
                    case 4:
                        System.out.println("Current salary: " + employee.getSalary());
                        BigDecimal newSalary = readBigDecimal("Enter the new salary: ");
                        if (employee.getSalary().equals(newSalary)) {
                            System.out.println("The new salary is the same as current salary.");
                            break;
                        }
                        String confirmSalary = readString("Do you want to change the actual salary $" + employee.getSalary() + " to $" + newSalary + "? (yes/no)");
                        if (!confirmSalary.equalsIgnoreCase("yes")) {
                            System.out.println("Update cancelled.");
                            return;
                        }
                        employee.setSalary(newSalary);
                        hasChanges = true;
                        System.out.println("Updated successfully.");
                        System.out.println(employee);
                        break;
                    case 0:
                        System.out.println("Exiting.");
                        editing = false;
                        break;
                    default:
                        System.out.println("Select a valid option.");
                        break;
                }
                try {
                    if (hasChanges) {
                        employeeService.updateEmployee(employee);
                        System.out.println("Updated successfully.");
                        hasChanges = false;
                    }
                } catch (FlowExecption e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (FlowExecption e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updatePassword() {
        try {
            authorizationManager.requireRole(currentToken, EmployeeRole.MANAGER);
            int getId = readInt("Type the employee ID for password update: ");
            Employee employee = findeEmployeeOrNull(getId);
            if (employee == null) {
                return;
            }
            System.out.println("Employee: " + employee.getUsername());
            String oldPassword;
            java.io.Console console = System.console();
            if (console != null) {
                oldPassword = new String(console.readPassword("Enter the current password: "));
            } else {
                oldPassword = readString("Enter the current password: ");
            }
            String newPassword;
            if (console != null) {
                newPassword = new String(console.readPassword("Enter the new password: "));
            } else {
                newPassword = readString("Enter the new password: ");
            }
            String confirmPassword;
            if (console != null) {
                confirmPassword = new String(console.readPassword("Confirm the new password: "));
            } else {
                confirmPassword = readString("Confirm the new password: ");
            }
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Password update cancelled.");
                return;
            }
            employeeService.changePassword(getId, oldPassword, newPassword);
            System.out.println("Password updated successfully.");
        } catch (FlowExecption e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void findAllEmployees() {
        if (employeeService.findAll().isEmpty()) {
            System.out.println("There are no employees found.");
        }
        System.out.println("All employees in our database: ");
        employeeService.findAll().forEach(System.out::println);
    }

    public void findEmployeeById() {
        int getId = readInt("Enter the employee ID: ");
        Employee employee = findeEmployeeOrNull(getId);
        if (employee == null) {
            return;
        }
        try {
            System.out.println("Employee found: ");
            employeeService.findById(getId);
        } catch (FlowExecption e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void findEmployeeByUsername() {
        String getUserName = readString("Enter username: ");
        employeeService.findByUsername(getUserName);
    }

    public void findInvalidEmployees() {
        if (employeeService.findInvalidEmployees().isEmpty()) {
            System.out.println("There are no invalid employees in our database");
            return;
        }
        System.out.println("All invalid employees in our database: ");
        employeeService.findInvalidEmployees().forEach(System.out::println);
    }

    public void applySalaryAdjustment() {
        int getId = readInt("Enter the employee ID: ");
        Employee employee = findeEmployeeOrNull(getId);
        if (employee == null) {
            return;
        }
        try {
            System.out.println(employee.getName() + " - Current role: " + employee.getRole());
            System.out.println("Current salary: $" + employee.getSalary());
            if (employee.getRole() ==  EmployeeRole.MANAGER) {
                System.out.println("Salary adjustment will be 20%");
                System.out.println("The new salary will be: $" + employee.getSalary().multiply(BigDecimal.valueOf(1.20)));
            } else if (employee.getRole() ==  EmployeeRole.TRAINER) {
                System.out.println("Salary adjustment will be 15%");
                System.out.println("The new salary will be: $" + employee.getSalary().multiply(BigDecimal.valueOf(1.15)));
            } else{
                System.out.println("Salary adjustment will be 10%");
                System.out.println("The new salary will be: $" + employee.getSalary().multiply(BigDecimal.valueOf(1.10)));
            }
            String confirm = readString("Do you want to apply a salary adjustment? (yes/no)");
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("Salary adjustment cancelled.");
                return;
            }
            employeeService.applySalaryAdjustment(employee);
            System.out.println("Salary adjustment applied successfully.");
            System.out.println("New salary: $" + employee.getSalary());
        } catch (FlowExecption e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteEmployee() {
        try {
            authorizationManager.requireRole(currentToken, EmployeeRole.MANAGER);
            int getId = readInt("Type the employee ID to delete: ");
            Employee employee = findeEmployeeOrNull(getId);
            if (employee == null) {
                return;
            }
            System.out.println("Employee to delete: ");
            System.out.println(employee);
            String confirm = readString("Are you sure you want to delete this employee? (yes/no)");
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }
            employeeService.deleteEmployee(getId);
            System.out.println("Employee deleted successfully.");
        } catch (FlowExecption e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private int readInt(String message) {
        while (true) {
            try {
                System.out.println(message);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid value");
            }
        }
    }

    private String readString(String message) {
        while (true) {
            System.out.println(message);
            String value = sc.nextLine();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Invalid input");
        }
    }

    private Double readDouble(String message) {
        while (true) {
            try {
                System.out.println(message);
                return Double.parseDouble(sc.nextLine().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Invalid value");
            }
        }
    }

    private BigDecimal readBigDecimal(String message) {
        while (true) {
            try {
                System.out.println(message);
                String input = sc.nextLine().replace(",", ".");
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid value");
            }
        }
    }

    private Employee findeEmployeeOrNull(Integer id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            System.out.println("User not found");
        }
        return employee;
    }

}
