package com.tuon.util.db;

import com.tuon.db.connection.DbConnection;
import com.tuon.exceptions.DbException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbCleaner {

    Connection conn = DbConnection.getConnection();
    public void cleanDatabase() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM workout_exercises");
            st.executeUpdate("DELETE FROM workouts");
            st.executeUpdate("DELETE FROM exercises");
            st.executeUpdate("DELETE FROM employees");
            st.executeUpdate("DELETE FROM gym_users");
            st.executeUpdate("DELETE FROM audit_log");
        } catch (SQLException e) {
            throw new DbException("Error cleaning database: " + e.getMessage());
        }
    }

}
