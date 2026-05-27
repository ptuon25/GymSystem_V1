// classe dos funcionários da academia

package com.tuon.entities;

import com.tuon.enums.EmployeeRole;

import java.time.LocalDateTime;

public class Employee {

    private Integer id;
    private String username;
    private String passwordHash;
    private String name;
    private EmployeeRole role;
    private Double salary;
    private LocalDateTime createdAt = LocalDateTime.now();

    // construtor vazio
    public Employee() {

    }

    // construtor padrão
    public Employee(Integer id, String username, String passwordHash, String name, EmployeeRole role, Double salary) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
        this.role = role;
        this.salary = salary;
    }

    // getters e setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // toString
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", salary=" + salary +
                '}';
    }

}
