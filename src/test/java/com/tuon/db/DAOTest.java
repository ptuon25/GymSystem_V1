package com.tuon.db;

import com.tuon.entities.Employee;
import com.tuon.entities.Exercise;
import com.tuon.entities.GymUser;
import com.tuon.entities.Workout;
import com.tuon.entities.WorkoutExercise;
import com.tuon.enums.Difficulty;
import com.tuon.enums.EmployeeRole;
import com.tuon.enums.WorkoutStatus;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DAOTest {

    public static void main(String[] args) {
        System.out.println("========== DAO TESTS ==========\n");

        try {
            System.out.println("1️⃣  Testing UserDAO...");
            testUserDAO();
            System.out.println("✅ UserDAO OK\n");

            System.out.println("2️⃣  Testing ExerciseDAO...");
            testExerciseDAO();
            System.out.println("✅ ExerciseDAO OK\n");

            System.out.println("3️⃣  Testing EmployeeDAO...");
            testEmployeeDAO();
            System.out.println("✅ EmployeeDAO OK\n");

            System.out.println("4️⃣  Testing WorkoutDAO...");
            testWorkoutDAO();
            System.out.println("✅ WorkoutDAO OK\n");

            System.out.println("========== ✅ ALL TESTS PASSED ==========");

        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection();
            System.out.println("\n🔌 Connection closed.");
        }
    }

    private static void testUserDAO() {
        Connection conn = DbConnection.getConnection();
        UserDAOImpl userDAO = new UserDAOImpl(conn);

        System.out.println("   → INSERT user...");
        GymUser user = new GymUser(null, "Ana Silva", 28, 1.65, 62.0);
        userDAO.insert(user);
        System.out.println("     ID: " + user.getId() + ", BMI: " + String.format("%.2f", user.calculateBMI()));

        System.out.println("   → FIND by ID " + user.getId() + "...");
        GymUser found = userDAO.findById(user.getId());
        System.out.println("     Found: " + found.getName());

        System.out.println("   → UPDATE weight to 65kg...");
        user.setWeight(65.0);
        userDAO.update(user);
        GymUser updated = userDAO.findById(user.getId());
        System.out.println("     New weight: " + updated.getWeight());

        System.out.println("   → FIND ALL...");
        List<GymUser> all = userDAO.findAll();
        System.out.println("     Total users: " + all.size());

        System.out.println("   → DELETE...");
        userDAO.deleteById(user.getId());
        System.out.println("     Deleted successfully");
    }

    private static void testExerciseDAO() {
        Connection conn = DbConnection.getConnection();
        ExerciseDAOImpl exerciseDAO = new ExerciseDAOImpl(conn);

        System.out.println("   → INSERT exercise...");
        // ✅ Corrigido: EASY em vez de INTERMEDIATE
        Exercise ex = new Exercise(null, "Bench Press", "CHEST", Difficulty.EASY);
        exerciseDAO.insert(ex);
        System.out.println("     ID: " + ex.getId() + ", Name: " + ex.getName());

        System.out.println("   → FIND by ID " + ex.getId() + "...");
        Exercise found = exerciseDAO.findById(ex.getId());
        System.out.println("     Found: " + found.getName() + " (" + found.getDifficulty() + ")");

        System.out.println("   → UPDATE difficulty to HARD...");
        // ✅ Corrigido: HARD em vez de ADVANCED
        ex.setDifficulty(Difficulty.HARD);
        exerciseDAO.update(ex);
        Exercise updated = exerciseDAO.findById(ex.getId());
        System.out.println("     New difficulty: " + updated.getDifficulty());

        System.out.println("   → FIND ALL...");
        List<Exercise> all = exerciseDAO.findAll();
        System.out.println("     Total exercises: " + all.size());

        System.out.println("   → DELETE...");
        exerciseDAO.deleteById(ex.getId());
        System.out.println("     Deleted successfully");
    }

    private static void testEmployeeDAO() {
        Connection conn = DbConnection.getConnection();
        EmployeeDAOImpl empDAO = new EmployeeDAOImpl(conn);

        System.out.println("   → INSERT employee...");
        // ✅ Corrigido: EmployeeRole.PERSONAL
        Employee emp = new Employee(null, "jsilva", "hashedPassword123", "Joao Silva", EmployeeRole.TRAINER, 3500.0);  // ✅ TRAINER em vez de PERSONAL
        empDAO.insert(emp);
        System.out.println("     ID: " + emp.getId() + ", Username: " + emp.getUsername());

        System.out.println("   → FIND by ID " + emp.getId() + "...");
        Employee found = empDAO.findById(emp.getId());
        System.out.println("     Found: " + found.getName() + " (" + found.getRole() + ")");

        System.out.println("   → FIND by USERNAME 'jsilva'...");
        Employee foundByUsername = empDAO.findByUsername("jsilva");
        System.out.println("     Found: " + foundByUsername.getName());

        System.out.println("   → UPDATE role to MANAGER...");
        emp.setRole(EmployeeRole.MANAGER);
        empDAO.update(emp);
        Employee updated = empDAO.findById(emp.getId());
        System.out.println("     New role: " + updated.getRole());

        System.out.println("   → FIND ALL...");
        List<Employee> all = empDAO.findAll();
        System.out.println("     Total employees: " + all.size());

        System.out.println("   → DELETE...");
        empDAO.deleteById(emp.getId());
        System.out.println("     Deleted successfully");
    }

    private static void testWorkoutDAO() {
        Connection conn = DbConnection.getConnection();
        WorkoutDAOimpl workoutDAO = new WorkoutDAOimpl(conn);
        ExerciseDAOImpl exerciseDAO = new ExerciseDAOImpl(conn);
        UserDAOImpl userDAO = new UserDAOImpl(conn);

        System.out.println("   → Setup: creating user...");
        GymUser user = new GymUser(null, "Carlos", 35, 1.80, 80.0);
        userDAO.insert(user);
        System.out.println("     User ID: " + user.getId());

        System.out.println("   → Setup: creating exercises...");
        // ✅ Corrigido: EASY e MEDIUM em vez de INTERMEDIATE e ADVANCED
        Exercise ex1 = new Exercise(null, "Squat", "LEGS", Difficulty.MEDIUM);
        Exercise ex2 = new Exercise(null, "Deadlift", "BACK", Difficulty.HARD);
        exerciseDAO.insert(ex1);
        exerciseDAO.insert(ex2);
        System.out.println("     Exercise 1 ID: " + ex1.getId() + ", Exercise 2 ID: " + ex2.getId());

        System.out.println("   → INSERT workout with exercises...");
        // ✅ Corrigido: construtor correto de Workout
        Workout workout = new Workout(null, "Leg Day", user.getId(), WorkoutStatus.PLANNED, LocalDateTime.now());        List<WorkoutExercise> exercises = new ArrayList<>();
        WorkoutExercise we1 = new WorkoutExercise(ex1, null, 4, 8, 100.0, 90, 1);
        WorkoutExercise we2 = new WorkoutExercise(ex2, null, 3, 5, 150.0, 120, 2);
        exercises.add(we1);
        exercises.add(we2);
        workout.setWorkoutExercises(exercises);

        workoutDAO.insert(workout);
        System.out.println("     Workout ID: " + workout.getId() + ", Total volume: " + String.format("%.2f", workout.calculateTotalVolume()));

        System.out.println("   → FIND by ID " + workout.getId() + "...");
        Workout found = workoutDAO.findById(workout.getId());
        System.out.println("     Found: " + found.getName() + " (" + found.getStatus() + ")");
        System.out.println("     Exercises: " + found.getWorkoutExercises().size());

        System.out.println("   → UPDATE status to IN_PROGRESS...");
        workout.setStatus(WorkoutStatus.IN_PROGRESS);        workoutDAO.update(workout);
        Workout updated = workoutDAO.findById(workout.getId());
        System.out.println("     New status: " + updated.getStatus());

        System.out.println("   → FIND ALL...");
        List<Workout> all = workoutDAO.findAll();
        System.out.println("     Total workouts: " + all.size());

        System.out.println("   → DELETE...");
        workoutDAO.deleteById(workout.getId());
        System.out.println("     Deleted successfully");

        System.out.println("   → Cleanup: deleting exercises and user...");
        exerciseDAO.deleteById(ex1.getId());
        exerciseDAO.deleteById(ex2.getId());
        userDAO.deleteById(user.getId());
        System.out.println("     Cleanup done");
    }
}