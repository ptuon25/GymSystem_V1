package com.tuon.db;

import com.tuon.db.connection.DbConnection;
import com.tuon.exceptions.DbException;
import com.tuon.util.db.DbCleaner;

import java.sql.Connection;

public class ConnectionTest {
    public static void main(String[] args) {
        System.out.println("========== DATABASE CONNECTION TEST ==========\n");
        try (Connection conn = DbConnection.getConnection()) {
            System.out.println("Attempting to connect to the database...");


            System.out.println("Connection object: " + conn);

            DbConnection.closeConnection();

            System.out.println("Closing connection...");

        } catch (Exception e) {
            System.err.println("An error occurred while connecting to the database: ");
            e.printStackTrace();
        }

        System.out.println("========== DATABASE CONNECTION TEST COMPLETED ==========");

        // Successfully test

    }
}
