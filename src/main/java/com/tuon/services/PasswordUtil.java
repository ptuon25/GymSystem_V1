package com.tuon.services;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Utilitário simples para hashing e verificação de senhas com BCrypt.
 * Ajuste WORK_FACTOR conforme potência da sua máquina. 10-12 é razoável.
 */
public final class PasswordUtil {

    // Work factor (cost). 10-12 é comum; 12 é seguro e ainda rápido em máquinas modernas.
    private static final int WORK_FACTOR = 12;

    private PasswordUtil() {}

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) throw new IllegalArgumentException("Password cannot be null");
        // hashToString produz algo como: $2a$12$... (inclui salt)
        return BCrypt.withDefaults().hashToString(WORK_FACTOR, plainPassword.toCharArray());
    }

    public static boolean verifyPassword(String plainPassword, String passwordHash) {
        if (plainPassword == null || passwordHash == null) return false;
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), passwordHash);
        return result.verified;
    }
}