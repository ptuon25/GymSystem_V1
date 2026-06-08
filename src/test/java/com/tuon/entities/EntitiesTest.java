package com.tuon.entities;

import com.tuon.enums.Difficulty;
import com.tuon.enums.EmployeeRole;
import com.tuon.enums.WorkoutStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntitiesTest {
    static void main() {
        System.out.println("========== ENTITIES OBJECT TEST ==========\n");

        Locale.setDefault(Locale.US);

        // Gym User
        try {
            System.out.println("Creating gym user test...");
            GymUser gymUser = new GymUser(null, "John Doe", 20, 1.85, 85.0);
            System.out.println("GymUser object: " + gymUser);
            System.out.println("GymUser Test OK ✅\n\n");

        } catch (Exception e) {
            System.err.println("Error occurred while testing GymUser ❌: " + e.getMessage());
        }

        // Employee
        try {
            System.out.println("Creating employee test...");
            Employee employee = new Employee(null, "jsmith", "123123", "John Smith", EmployeeRole.TRAINER, BigDecimal.valueOf(3000.00));
            System.out.println("Employee object: " + employee);
            System.out.println("Employee Test OK ✅\n\n");
        } catch (Exception e) {
            System.err.println("Error occurred while testing Employee ❌: " + e.getMessage());
        }

        // Exercise
        try {
            System.out.println("Creating Exercise test...");
            List<Exercise> exercises = new ArrayList<>();
            exercises.add(new Exercise(null, "Bench Press", "Chest exercise", Difficulty.MEDIUM));
            exercises.add(new Exercise(null, "Squat", "Leg exercise", Difficulty.HARD));
            exercises.forEach(System.out::println);
            System.out.println("Exercise Test OK ✅\n\n");
        } catch (Exception e) {
            System.err.println("Error occurred while testing Exercise ❌: " + e.getMessage());
        }

        // WorkoutExercise
        try {
            System.out.println("Creating WorkoutExercise test...");
            Exercise exercise = new Exercise(null, "Deadlift", "Back exercise", Difficulty.HARD);
            WorkoutExercise workoutExercise = new WorkoutExercise(null, exercise, null, 4, 8, 100.0, 90, 1);
            System.out.println("WorkoutExercise object: " + workoutExercise);
            System.out.println("WorkoutExercise Test OK ✅\n\n");
        } catch (Exception e) {
            System.err.println("Error occurred while testing WorkoutExercise ❌: " + e.getMessage());
        }

        // Workout
        try {
            System.out.println("Creating Workout test...");

            Exercise exercise1 = new Exercise(null, "Pull-up", "Back exercise", Difficulty.MEDIUM);
            Exercise exercise2 = new Exercise(null, "Lunges", "Leg exercise", Difficulty.MEDIUM);
            Workout workout = new Workout(null, "Workout 01", null, WorkoutStatus.PLANNED, LocalDateTime.now());
            workout.addWorkoutExercise(new WorkoutExercise(null, exercise1, null, 3, 10, 20.0, 60, 1));
            workout.addWorkoutExercise(new WorkoutExercise(null, exercise2, null, 3, 12, 15.0, 60, 2));
            System.out.println(workout);
            System.out.println("Total volume: " + workout.calculateTotalVolume());
            System.out.println("Workout Test OK ✅\n\n");
        } catch (Exception e) {
            System.err.println("Error occurred while testing Workout ❌: " + e.getMessage());
        }

        System.out.println("========== ENTITIES OBJECT TEST COMPLETED ==========");
        // Successfully test
    }
}
