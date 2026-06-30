package com.tuon.app.auth;

import com.tuon.entities.Employee;

import java.time.Instant;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gerencia sessões em memória. Para app console mono-usuário isso basta.
 * Para multi-usuário ou produção considere persistência e refresh tokens.
 */
public class SessionManager {

    public static class Session {
        private final String token;
        private final Employee employee;
        private final Instant createdAt;
        private final Instant expiresAt;

        public Session(String token, Employee employee, Instant createdAt, Instant expiresAt) {
            this.token = token;
            this.employee = employee;
            this.createdAt = createdAt;
            this.expiresAt = expiresAt;
        }

        public String getToken() { return token; }
        public Employee getEmployee() { return employee; }
        public Instant getCreatedAt() { return createdAt; }
        public Instant getExpiresAt() { return expiresAt; }
        public boolean isExpired() { return Instant.now().isAfter(expiresAt); }
    }

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    // Sessões duram, por exemplo, 30 minutos
    private final Duration sessionDuration = Duration.ofMinutes(30);

    public String createSession(Employee employee) {
        // clone minimo do employee (não incluir passwordHash)
        employee.setPasswordHash(null);
        String token = UUID.randomUUID().toString();
        Instant now = Instant.now();
        Session s = new Session(token, employee, now, now.plus(sessionDuration));
        sessions.put(token, s);
        return token;
    }

    public Session getSession(String token) {
        if (token == null) return null;
        Session s = sessions.get(token);
        if (s == null) return null;
        if (s.isExpired()) {
            sessions.remove(token);
            return null;
        }
        return s;
    }

    public Employee getEmployeeForToken(String token) {
        Session s = getSession(token);
        return s == null ? null : s.getEmployee();
    }

    public void invalidate(String token) {
        if (token != null) sessions.remove(token);
    }
}