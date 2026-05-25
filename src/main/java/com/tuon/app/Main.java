// teste de conexão concluído
// teste de CRUD usuário concluído
// teste de CRUD employee concluído
// teste de CRUD exercise concluído

package com.tuon.app;

import com.tuon.db.DbConnection;
import com.tuon.db.UserDAO;
import com.tuon.db.UserDAOImpl;
import com.tuon.entities.GymUser;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    static void main() {

        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        System.out.println("====Gym System V1====\n\n");

        System.out.println("Testing database connection...\n");

        DbConnection.getConnection();

        System.out.println("Database connection successful!\n");

        var gymUser = new GymUser(null, "John Doe", 30, 1.75, 70.0);
        System.out.println("Created GymUser: " + gymUser);

        sc.close();

        DbConnection.closeConnection();

        System.out.println("\nDatabase connection closed.");
    }
}
