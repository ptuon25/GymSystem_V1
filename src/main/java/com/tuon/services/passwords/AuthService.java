package com.tuon.services.passwords;

import com.tuon.db.DAO.EmployeeDAO;
import com.tuon.entities.Employee;
import com.tuon.exceptions.AuthException;

/**
 * Serviço de autenticação (simples). Não emite tokens — retorna o Employee autenticado.
 * Em um próximo passo você pode gerar JWT / sessão.
 */
public class AuthService {

    private final EmployeeDAO employeeDao;

    public AuthService(EmployeeDAO employeeDao) {
        this.employeeDao = employeeDao;
    }

    /**
     * Autentica usuário por username e password (plaintext).
     * Retorna o Employee se credenciais corretas, senão lança UnauthorizedException (ou retorna null conforme sua política).
     */
    public Employee authenticate(String username, String plainPassword) {
        if (username == null || plainPassword == null) {
            throw new IllegalArgumentException("username and password are required");
        }

        Employee emp = employeeDao.findByUsername(username);
        if (emp == null) {
            // Não vazar que o usuário não existe; apenas indicar credenciais inválidas
            throw new AuthException("Invalid credentials");
        }

        String storedHash = emp.getPasswordHash();
        boolean ok = PasswordUtil.verifyPassword(plainPassword, storedHash);
        if (!ok) {
            throw new AuthException("Invalid credentials");
        }

        // Opcional: limpar passwordHash do objeto retornado (não expor)
        emp.setPasswordHash(null);
        return emp;
    }

    /**
     * Cria um novo employee (faz hashing da senha antes de persistir).
     * Recebe a entidade Employee com username/name/role/salary e o plainPassword.
     */
    public Employee registerEmployee(Employee employee, String plainPassword) {
        if (employee == null || plainPassword == null) {
            throw new IllegalArgumentException("Employee and password required");
        }
        String hash = PasswordUtil.hashPassword(plainPassword);
        employee.setPasswordHash(hash);
        employeeDao.insert(employee); // EmployeeDAOImpl deve setar o id gerado no objeto
        // opcional: remover passwordHash do objeto retornado
        employee.setPasswordHash(null);
        return employee;
    }

    /**
     * Trocar senha: valida a antiga e atualiza para a nova (hash).
     */
    public void changePassword(String username, String oldPassword, String newPassword) {
        Employee emp = employeeDao.findByUsername(username);
        if (emp == null) throw new AuthException("User not found");
        if (!PasswordUtil.verifyPassword(oldPassword, emp.getPasswordHash())) {
            throw new AuthException("Invalid current password");
        }
        String newHash = PasswordUtil.hashPassword(newPassword);
        emp.setPasswordHash(newHash);
        employeeDao.update(emp);
    }
}