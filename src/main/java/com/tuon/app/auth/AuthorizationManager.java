package com.tuon.app.auth;

import com.tuon.entities.Employee;
import com.tuon.enums.EmployeeRole;
import com.tuon.exceptions.AuthException;

import java.util.Arrays;
import java.util.Objects;

public class AuthorizationManager {

    private final SessionManager sessionManager;

    public AuthorizationManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Retorna o Employee autenticado para o token, ou lança AuthException.
     */
    public Employee requireAuthenticated(String token) {
        Employee emp = sessionManager.getEmployeeForToken(token);
        if (emp == null) throw new AuthException("Not authenticated or session expired");
        return emp;
    }

    /**
     * Assegura que o usuário tem um dos roles permitidos.
     */
    public void requireRole(String token, EmployeeRole... allowed) {
        Employee emp = requireAuthenticated(token);
        if (allowed == null || allowed.length == 0) return;
        boolean ok = Arrays.stream(allowed).anyMatch(r -> Objects.equals(emp.getRole(), r));
        if (!ok) throw new AuthException("Unauthorized: requires one of roles " + Arrays.toString(allowed));
    }

    /**
     * Checa se o usuário é o próprio recurso ou tem um role superior (útil para permitir edição própria).
     * Ex: allow if employee id == resourceOwnerId OR role == MANAGER
     */
    public void requireSelfOrRole(String token, Integer resourceOwnerId, EmployeeRole... allowedRoles) {
        Employee emp = requireAuthenticated(token);
        if (emp.getId() != null && emp.getId().equals(resourceOwnerId)) return;
        boolean ok = Arrays.stream(allowedRoles).anyMatch(r -> r.equals(emp.getRole()));
        if (!ok) throw new AuthException("Unauthorized");
    }
}