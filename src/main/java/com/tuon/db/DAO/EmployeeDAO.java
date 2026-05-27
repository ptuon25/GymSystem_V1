package com.tuon.db.DAO;

import com.tuon.entities.Employee;

import java.util.List;

public interface EmployeeDAO {

        void insert(Employee employee);

        void update(Employee employee);

        void deleteById(Integer id);

        Employee findById(Integer id);

        List<Employee> findAll();

        Employee findByUsername(String username);

}
