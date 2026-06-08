package com.tuon.db;

import com.tuon.db.DAO.*;
import com.tuon.db.DAOImpl.*;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.*;
import com.tuon.enums.Difficulty;
import com.tuon.enums.EmployeeRole;
import com.tuon.enums.WorkoutStatus;
import com.tuon.exceptions.DbException;
import com.tuon.logs.audit.*;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DAOTest {

    public static void main(String[] args) {

        System.out.println("========== DAO TESTS ==========\n");

        try {
            Connection conn = DbConnection.getConnection();
            System.out.println("Database connection established ✅\n");

            //GymUser DAO Test
            try {
                System.out.println("========== GymUserDAO TEST ==========\n");
                GymUserDAO gymUserDAO = new GymUserDAOImpl(conn);
                GymUser gymUser = new GymUser(null, "John Doe", 20, 1.85, 85.0);

                System.out.println("\nTesting insert....");
                gymUserDAO.insert(gymUser);
                if (gymUser.getId() != null) {
                    System.out.println("ID generated successfully: " + gymUser.getId());
                } else {
                    System.err.println("Failed to generate ID for GymUser.");
                }

                System.out.println("\nTesting update....");
                gymUser.setAge(21);
                gymUserDAO.update(gymUser);
                System.out.println("Updated GymUser: " + gymUser);

                System.out.println("\nTesting findAll....");
                gymUserDAO.findAll().forEach(System.out::println);

                System.out.println("\nTesting findById....");
                GymUser found = gymUserDAO.findById(gymUser.getId());
                System.out.println("Found GymUser by ID: " + found);

                System.out.println("\nTesting delete....");
                gymUserDAO.deleteById(gymUser.getId());
                System.out.println("Deleted GymUser with ID: " + gymUser.getId());

                System.out.println("GymUserDAO Test OK ✅\n\n");
            } catch (Exception e) {
                System.err.println("GymUserDAO Test FAILED ❌");
                e.printStackTrace();
            }

            //Employee DAO Test
            try {
                System.out.println("========== EmployeeDAO TEST ==========\n");
                EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
                Employee employee = new Employee(null, "jsmith", "123123", "John Smith", EmployeeRole.TRAINER, BigDecimal.valueOf(3000.00));

                System.out.println("\nTesting insert....");
                employeeDAO.insert(employee);
                if (employee.getId() != null) {
                    System.out.println("ID generated successfully: " + employee.getId());
                } else {
                    System.err.println("Failed to generate ID for Employee.");
                }

                System.out.println("\nTesting update....");
                employee.setSalary(BigDecimal.valueOf(3200.00));
                employeeDAO.update(employee);
                System.out.println("Updated Employee: " + employee);

                System.out.println("\nTesting findAll....");
                employeeDAO.findAll().forEach(System.out::println);

                System.out.println("\nTesting findById....");
                Employee found = employeeDAO.findById(employee.getId());
                System.out.println("Found Employee by ID: " + found);

                System.out.println("\nTesting findByUsername....");
                Employee foundUser = employeeDAO.findByUsername(employee.getUsername());
                System.out.println("Found Employee by username: " + foundUser);

                System.out.println("\nTesting delete....");
                employeeDAO.deleteById(employee.getId());
                System.out.println("Deleted Employee with ID: " + employee.getId());

                System.out.println("EmployeeDAO Test OK ✅\n\n");
            } catch (Exception e) {
                System.err.println("EmployeeDAO Test FAILED ❌");
                e.printStackTrace();
            }

            // Exercise DAO test
            try {
                System.out.println("========== ExerciseDAO TEST ==========\n");
                ExerciseDAO exerciseDAO = new ExerciseDAOImpl(conn);
                Exercise exercise = new Exercise(null, ")Bench Press", "Chest exercise", Difficulty.MEDIUM);

                System.out.println("\nTesting insert....");
                exerciseDAO.insert(exercise);
                if (exercise.getId() != null) {
                    System.out.println("ID generated successfully: " + exercise.getId());
                } else {
                    System.err.println("Failed to generate ID for Exercise.");
                }

                System.out.println("\nTesting update....");
                exercise.setDifficulty(Difficulty.HARD);
                exerciseDAO.update(exercise);
                System.out.println("Updated Exercise: " + exercise);

                System.out.println(("\nTesting findAll...."));
                exerciseDAO.findAll().forEach(System.out::println);

                System.out.println("\nTesting findById....");
                Exercise found = exerciseDAO.findById(exercise.getId());
                System.out.println("Found Exercise by ID: " + found);

                System.out.println("\nTesting findExercisesByWorkoutId....");
                GymUserDAO gymUserDAO = new GymUserDAOImpl(conn);
                GymUser gymUser = new GymUser(null, "John Doe", 20, 1.85, 85.0);
                gymUserDAO.insert(gymUser);
                WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
                Workout workout = new Workout(null, "Workout 01", gymUser.getId(), WorkoutStatus.PLANNED, LocalDateTime.now());
                workoutDAO.insert(workout);
                WorkoutExerciseDAO workoutExerciseDAO = new WorkoutExerciseDAOImpl(conn);
                WorkoutExercise workoutExercise = new WorkoutExercise(null, exercise, workout.getId(), 4, 5, 75.00, 60, 1);
                workoutExerciseDAO.insert(workoutExercise);
                List<Exercise> exercises = exerciseDAO.findExercisesByWorkoutId(workout.getId());
                System.out.println("Found Exercises by Workout ID: " + exercises);
                workoutExerciseDAO.deleteByWorkoutAndExercise(workout.getId(), exercise.getId()); // don't need to retest in the WorkoutExercise block
                workoutDAO.deleteById(workout.getId());
                gymUserDAO.deleteById(gymUser.getId());

                System.out.println("\nTesting deleteById....");
                exerciseDAO.deleteById(exercise.getId());
                System.out.println("Deleted Exercise with ID: " + exercise.getId());

                System.out.println("ExerciseDAO Test OK ✅\n\n");
            } catch (Exception e) {
                System.err.println("ExerciseDAO Test FAILED ❌");
                e.printStackTrace();
            }

            // WorkoutExercise DAO test
            try {
                GymUserDAO gymUserDAO = new GymUserDAOImpl(conn);
                GymUser gymUser = new GymUser(null, "John Doe", 20, 1.85, 85.0);
                gymUserDAO.insert(gymUser);
                WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
                Workout workout = new Workout(null, "Workout 01", gymUser.getId(), WorkoutStatus.PLANNED, LocalDateTime.now());
                workoutDAO.insert(workout);
                ExerciseDAO exerciseDAO = new ExerciseDAOImpl(conn);
                Exercise exercise = new Exercise(null, "Bench Press", "Chest exercise", Difficulty.MEDIUM);
                exerciseDAO.insert(exercise);
                WorkoutExerciseDAO workoutExerciseDAO = new WorkoutExerciseDAOImpl(conn);
                WorkoutExercise workoutExercise = new WorkoutExercise(null, exercise, workout.getId(), 4, 5, 75.00, 60, 1);

                System.out.println("\nTesting insert....");
                workoutExerciseDAO.insert(workoutExercise);
                if (workoutExercise.getId() != null) {
                    System.out.println("ID generated successfully: " + workoutExercise.getId());
                } else {
                    System.err.println("Failed to generate ID for WorkoutExercise.");
                }

                System.out.println("\nTesting update....");
                workoutExercise.setReps(6);
                workoutExercise.setWeight(80.00);
                workoutExerciseDAO.update(workoutExercise);
                System.out.println("Updated WorkoutExercise: " + workoutExercise);

                System.out.println("\nTesting findByWorkoutId....");
                workoutExerciseDAO.findByWorkoutId(workout.getId()).forEach(System.out::println);

                System.out.println("\nTesting deleteByWorkoutId....");
                workoutExerciseDAO.deleteByWorkoutId(workout.getId());
                System.out.println("Deleted WorkoutExercises with Workout ID: " + workout.getId());
                workoutDAO.deleteById(workout.getId());
                exerciseDAO.deleteById(exercise.getId());
                gymUserDAO.deleteById(gymUser.getId());

                System.out.println("WorkoutExerciseDAO Test OK ✅\n\n");
            } catch (Exception e) {
                System.err.println("WorkoutExerciseDAO Test FAILED ❌");
                e.printStackTrace();
            }

            // Workout DAO test
            try {
                System.out.println("========== WorkoutDAO TEST ==========\n");
                GymUserDAO gymUserDAO = new GymUserDAOImpl(conn);
                GymUser gymUser = new GymUser(null, "John Doe", 20, 1.85, 85.0);
                gymUserDAO.insert(gymUser);
                WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
                Workout workout = new Workout(null, "Workout 01", gymUser.getId(), WorkoutStatus.PLANNED, LocalDateTime.now());

                System.out.println("\nTesting insert....");
                workoutDAO.insert(workout);
                if (workout.getId() != null) {
                    System.out.println("ID generated successfully: " + workout.getId());
                } else {
                    System.err.println("Failed to generate ID for Workout.");
                }
                System.out.println("Inserted Workout: " + workout);

                System.out.println("\nTesting update....");
                workout.setStatus(WorkoutStatus.COMPLETED);
                workoutDAO.update(workout);
                System.out.println("Updated Workout: " + workout);

                System.out.println("\nTesting findAll....");
                workoutDAO.findAll().forEach(System.out::println);

                System.out.println("\nTesting findById....");
                Workout found = workoutDAO.findById(workout.getId());
                System.out.println("Found Workout by ID: " + found);

                System.out.println("\nTesting deleteById....");
                workoutDAO.deleteById(workout.getId());
                gymUserDAO.deleteById(gymUser.getId());
                System.out.println("Deleted Workout with ID: " + workout.getId());

                System.out.println("WorkoutDAO Test OK ✅\n\n");
            } catch (Exception e) {
                System.err.println("WorkoutDAO Test FAILED ❌");
                e.printStackTrace();
            }

            // AuditLog DAO test
            System.out.println("========== AuditLogDAO TEST ==========\n");
            try {
                EmployeeDAO employeeDAO = new EmployeeDAOImpl(conn);
                Employee employee = new Employee(null, "jsmith", "123123", "John Smith", EmployeeRole.TRAINER, BigDecimal.valueOf(3000.00));
                employeeDAO.insert(employee);
                AuditLogDAO auditLogDAO = new AuditLogDAOImpl(conn);
                AuditLog auditLog = new AuditLog(null, employee.getId(), AuditAction.CREATE, EntityType.EMPLOYEE, employee.getId(), "Successful create", true, LocalDateTime.now());

                System.out.println("\nTesting insert....");
                auditLogDAO.insert(auditLog);
                if (auditLog.getId() != null) {
                    System.out.println("ID generated successfully: " + auditLog.getId());
                } else {
                    System.err.println("Failed to generate ID for AuditLog.");
                }

                System.out.println("\nTesting findAll....");
                auditLogDAO.findAll().forEach(System.out::println);

                System.out.println("\nTesting findByEmployee....");
                List<AuditLog> found = auditLogDAO.findByEmployee(employee.getId());
                System.out.println("Found AuditLog by Employee ID: " + found);

                System.out.println("\nTesting findByAction....");
                List<AuditLog> foundAction = auditLogDAO.findByAction(AuditAction.CREATE);
                System.out.println("Found AuditLog by Action: " + foundAction);

                System.out.println("\nTesting findByEntityType....");
                List<AuditLog> foundType = auditLogDAO.findByEntityType(EntityType.EMPLOYEE);
                System.out.println("Found AuditLog by Entity Type: " + foundType);

                System.out.println("\nTesting findByDateRange....");
                LocalDateTime start = LocalDateTime.now().minusDays(1);
                LocalDateTime end = LocalDateTime.now().plusDays(1);
                List<AuditLog> foundDateRange = auditLogDAO.findByDateRange(start, end);
                System.out.println("Found AuditLog by Date Range: " + foundDateRange);

                System.out.println("\nTesting findByEmployeeAndAction....");
                List<AuditLog> foundEmployeeAction = auditLogDAO.findByEmployeeAndAction(employee.getId(), AuditAction.CREATE);
                System.out.println("Found AuditLog by Employee and Action: " + foundEmployeeAction);

                employeeDAO.deleteById(employee.getId());

                System.out.println("AuditLogDAO Test OK ✅\n\n");
            } catch (Exception e) {
                System.err.println("AuditLogDAO Test FAILED ❌");
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new DbException("Error occurred while testing DAOs: " + e.getMessage());
        } finally {
            DbConnection.closeConnection();
            System.out.println("Database connection closed ✅\n");
            System.out.println("========== DAO TESTS COMPLETED ==========");
            // Successfully test
        }
    }
}