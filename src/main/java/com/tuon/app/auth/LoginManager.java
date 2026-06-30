package com.tuon.app.auth;

import com.tuon.db.DAO.EmployeeDAO;
import com.tuon.db.DAOImpl.EmployeeDAOImpl;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Employee;
import com.tuon.exceptions.AuthException;
import com.tuon.services.passwords.AuthService;

import java.sql.Connection;

/**
 * Responsável por realizar login/logout usando AuthService e SessionManager.
 */
public class LoginManager {

    private final SessionManager sessionManager;

    public LoginManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Faz login e retorna token de sessão (UUID) se sucesso.
     * Lembre-se de fechar conexão com DB após operação (usando finally).
     */
    public String login(String username, String plainPassword) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            EmployeeDAO dao = new EmployeeDAOImpl(conn);
            AuthService auth = new AuthService(dao);
            Employee emp = auth.authenticate(username, plainPassword); // emp.passwordHash já é nulificado no AuthService
            return sessionManager.createSession(emp);
        } catch (AuthException ae) {
            throw ae;
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public void logout(String token) {
        sessionManager.invalidate(token);
    }
}