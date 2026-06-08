package com.tuon.services;

import com.tuon.entities.*;
import com.tuon.enums.Difficulty;
import com.tuon.enums.EmployeeRole;
import com.tuon.enums.WorkoutStatus;
import com.tuon.exceptions.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicesTest {
    static void main() {

        System.out.println("\n========== SERVICE TESTS START ==========\n");

        // Gym Service test
        try {
            System.out.println("========== USER TESTS ==========\n");
            GymUserService gymUserService = new GymUserService();
            GymUser user1 = new GymUser(null, "John Doe", 20, 1.78, 80.00);
            GymUser user2 = new GymUser(null, "Jane Doe", 25, 1.65, 60.00);

            System.out.println("Creating user...");
            gymUserService.createUser(user1);
            gymUserService.createUser(user2);
            gymUserService.findAll().forEach(System.out::println);

            System.out.println("\nUpdating user...");
            user1.setWeight(82.00);
            user2.setAge(26);
            gymUserService.updateUser(user1);
            gymUserService.updateUser(user2);
            gymUserService.findAll().forEach(System.out::println);

            System.out.println("\nFinding by id....");
            GymUser foundUser = gymUserService.findById(user1.getId());
            System.out.println(foundUser);

            System.out.println("\nFind all users...");
            gymUserService.findAll().forEach(System.out::println);

            System.out.println("\nSorting by fitness level...");
            List<GymUser> sortedUsers = gymUserService.getUsersOrderedByBMI();
            sortedUsers.forEach(user -> System.out.printf("%s - BMI: %.2f - Level: %s%n", user.getName(), user.calculateBMI(), user.bmiClassification()));

            System.out.println("\nFinding all invalid users...");
            List<GymUser> invalidUsers = gymUserService.findInvalidUsers();
            if (invalidUsers.isEmpty()) {
                System.out.println("No invalid users found ✅");
            } else {
                System.out.println("Invalid users found ⚠️");
                invalidUsers.forEach(System.out::println);
            }

            System.out.println("\nDeleting user...");
            gymUserService.deleteUser(user1.getId());
            gymUserService.deleteUser(user2.getId());
            gymUserService.findAll().forEach(System.out::println);

            System.out.println("GymUser Service Test OK ✅");
        } catch (Exception e) {
            System.err.println("Error during user tests: " + e.getMessage());
        } finally {
            System.out.println("\n========== USER TESTS END ==========\n");
        }

        // Employee Service test
        try {
            System.out.println("========== EMPLOYEE TESTS ==========\n");

            EmployeeService employeeService = new EmployeeService();
            Employee employee1 = new Employee(null, "jsmith", null, "John Smith", EmployeeRole.TRAINER, BigDecimal.valueOf(3000.00));
            Employee employee2 = new Employee(null, "adoe", null, "Amelia Doe", EmployeeRole.MANAGER, BigDecimal.valueOf(4000.00));

            System.out.println("Creating employee...");
            employeeService.createEmployee(employee1, "012012");
            employeeService.createEmployee(employee2, "123123");
            employeeService.findAll().forEach(System.out::println);

            System.out.println("\nUpdating employee...");
            employee1.setSalary(BigDecimal.valueOf(3200.00));
            employee2.setRole(EmployeeRole.TRAINER);
            employeeService.updateEmployee(employee1);
            employeeService.updateEmployee(employee2);
            employeeService.findAll().forEach(System.out::println);

            System.out.println("\nChanging password...");
            employeeService.changePassword(employee1.getId(), "012012", "newpassword123");
            employeeService.findAll().forEach(System.out::println);

            System.out.println("\nFinding by id...");
            Employee foundEmployee = employeeService.findById(employee1.getId());
            System.out.println(foundEmployee);

            System.out.println("\nFinding all employees...");
            employeeService.findAll().forEach(System.out::println);

            System.out.println("\nFinding by username...");
            Employee foundUser = employeeService.findByUsername(employee1.getUsername());
            System.out.println(foundUser);

            System.out.println("\nFinding invalid employees...");
            List<Employee> invalidEmployees = employeeService.findInvalidEmployees();
            if (invalidEmployees.isEmpty()) {
                System.out.println("No invalid employees found ✅");
            } else {
                System.out.println("Invalid employees found ⚠️");
                invalidEmployees.forEach(System.out::println);
            }

            System.out.println("\nCalculating Adjusted Salary...");
            employeeService.applySalaryAdjustment(employee1);
            System.out.printf("Adjusted Salary: %.2f%n", employee1.getSalary());

            System.out.println("\nDeleting employee...");
            employeeService.deleteEmployee(employee1.getId());
            employeeService.deleteEmployee(employee2.getId());
            employeeService.findAll().forEach(System.out::println);

            System.out.println("Employee Service Test OK ✅");
        } catch (Exception e) {
            System.err.println("Error during employee tests: " + e.getMessage());
        } finally {
            System.out.println("\n========== EMPLOYEE TESTS END ==========\n");
        }

        // Exercise Service test
        try {
            System.out.println("========== EXERCISE TESTS ==========\n");
            ExerciseService exerciseService = new ExerciseService();
            List<Exercise> exerciseList = new ArrayList<>();
            exerciseList.add(new Exercise(null, "Push-up", "Chest", Difficulty.EASY));
            exerciseList.add(new Exercise(null, "Squat", "Legs", Difficulty.MEDIUM));
            exerciseList.add(new Exercise(null, "Deadlift", "Back", Difficulty.HARD));

            System.out.println("\nCreating exercises...");
            exerciseList.forEach(exerciseService::createExercise);
            exerciseService.findAll().forEach(System.out::println);
            exerciseList.forEach(exercise -> System.out.println(exercise.getName() + " ID: " + exercise.getId()));

            System.out.println("\nUpdating exercises...");
            exerciseList.get(0).setDifficulty(Difficulty.MEDIUM);
            exerciseList.get(1).setMuscleGroup("Lower Body");
            exerciseList.get(2).setName("Barbell Deadlift");
            exerciseList.forEach(exerciseService::updateExercise);
            exerciseService.findAll().forEach(System.out::println);

            System.out.println("\nFinding by id...");
            Exercise foundExercise = exerciseService.findById(exerciseList.get(0).getId());
            System.out.println(foundExercise);

            System.out.println("\nFinding all exercises...");
            exerciseService.findAll().forEach(System.out::println);

            System.out.println("\nFinding by difficulty and muscle group...");
            List<Exercise> filteredExercises = exerciseService.findByDifficultyAndMuscleGroup(Difficulty.MEDIUM, "Lower Body");
            filteredExercises.forEach(System.out::println);

            System.out.println("\nFinding by best combination...");
            List<Exercise> bestComb = exerciseService.bestCombination(Difficulty.MEDIUM, "Lower Body");
            bestComb.forEach(System.out::println);

            System.out.println("\nDeleting exercises...");
            List<Exercise> remaining = exerciseService.findAll();
            if (remaining.isEmpty()) {
                System.out.println("No exercises remaining ✅");
            } else {
                remaining.forEach(System.out::println);
            }

            System.out.println("Exercise Service Test OK ✅");
        } catch (Exception e) {
            System.err.println("Error during exercise tests: " + e.getMessage());
        } finally {
            System.out.println("\n========== EXERCISE TESTS END ==========\n");
        }

        // WorkoutExercise Service test
        try {
            System.out.println("========== WORKOUT EXERCISE TESTS ==========\n");
            GymUserService gymUserService = new GymUserService();
            GymUser gymUser = new GymUser(null, "John Doe", 28, 1.85, 90.0);
            gymUserService.createUser(gymUser);
            WorkoutService workoutService = new WorkoutService();
            Workout workout = new Workout(null, "Full Body Workout", gymUser.getId(), WorkoutStatus.PLANNED, LocalDateTime.now());
            workoutService.createWorkout(workout);
            ExerciseService exerciseService = new ExerciseService();
            Exercise exercise1 = new Exercise(null, "Push-up", "Chest", Difficulty.EASY);
            Exercise exercise2 = new Exercise(null, "Squat", "Legs", Difficulty.MEDIUM);
            exerciseService.createExercise(exercise1);
            exerciseService.createExercise(exercise2);

            System.out.println("\nCreating workout...");
            WorkoutExerciseService workoutExerciseService = new WorkoutExerciseService();
            WorkoutExercise workoutExercise1 = new WorkoutExercise(null, exercise1, workout.getId(), 3, 12, 120.00, 60, 1);
            WorkoutExercise workoutExercise2 = new WorkoutExercise(null, exercise2, workout.getId(), 4, 10, 100.00, 60, 2);
            workoutExerciseService.createWorkoutExercise(workoutExercise1);
            workoutExerciseService.createWorkoutExercise(workoutExercise2);
            workoutExerciseService.findAll().forEach(System.out::println);

            System.out.println("\nUpdating workout exercise...");
            workoutExercise1.setReps(15);
            workoutExercise2.setSets(5);
            workoutExerciseService.updateWorkoutExercise(workoutExercise1);
            workoutExerciseService.updateWorkoutExercise(workoutExercise2);
            workoutExerciseService.findAll().forEach(System.out::println);

            System.out.println("\nFinding all...");
            workoutExerciseService.findAll().forEach(System.out::println);

            System.out.println("\nFinding by Workout and Exercise...");
            WorkoutExercise found = workoutExerciseService.findByWorkoutAndExercise(workout.getId(), exercise1.getId());
            System.out.println(found);

            System.out.println("\nFinding all invalid exercises...");
            List<WorkoutExercise> invalidWorkoutExercises = workoutExerciseService.findAllInvalidExercises();
            if (invalidWorkoutExercises.isEmpty()) {
                System.out.println("No invalid workout exercises found ✅");
            } else {
                System.out.println("Invalid workout exercises found ⚠️");
                invalidWorkoutExercises.forEach(System.out::println);
            }

            System.out.println("\nBatch insert....");
            Exercise exercise3 = new Exercise(null, "Squat", "Legs", Difficulty.MEDIUM);
            Exercise exercise4 = new Exercise(null, "Deadlift", "Back", Difficulty.HARD);
            exerciseService.createExercise(exercise3);
            exerciseService.createExercise(exercise4);
            WorkoutExercise workoutExercise3 = new WorkoutExercise(null, exercise3, workout.getId(), 3, 12, 120.00, 60, 1);
            WorkoutExercise workoutExercise4 = new WorkoutExercise(null, exercise4, workout.getId(), 4, 10, 100.00, 60, 2);
            List<WorkoutExercise> batch = new ArrayList<>();
            batch.add(workoutExercise3);
            batch.add(workoutExercise4);
            workoutExerciseService.batchInsert(batch);
            workoutExerciseService.findAll().forEach(System.out::println);

            System.out.println("\nFinding heaviest exercises....");
            List<WorkoutExercise> heaviest = workoutExerciseService.findHeaviestExercises();
            heaviest.forEach(System.out::println);

            System.out.println("\nFinding longest rest exercises....");
            List<WorkoutExercise> longestRest = workoutExerciseService.findLongestRestExercises();
            longestRest.forEach(System.out::println);

            System.out.println("\nFinding all ordered....");
            List<WorkoutExercise> ordered = workoutExerciseService.findAllOrdered();
            ordered.forEach(System.out::println);

            System.out.println("\nFinding exercises by workout");
            List<WorkoutExercise> findByWorkout = workoutExerciseService.findExercisesByWorkout(workout.getId());

            try {
                System.out.println("\nDeleting workout exercises...");
                List<WorkoutExercise> findByWorkoutDelete = workoutExerciseService.findExercisesByWorkout(workout.getId());
                for (WorkoutExercise we : findByWorkoutDelete) {
                    workoutExerciseService.deleteWorkoutExercise(we.getWorkoutId(), we.getExercise().getId());
                }
                System.out.println("Workout Exercises Test ok ✅");
            } catch (Exception e) {
                System.err.println("Error during deletion of workout exercises: " + e.getMessage());
            } finally {
                exerciseService.deleteExercise(exercise1.getId());
                exerciseService.deleteExercise(exercise2.getId());
                exerciseService.deleteExercise(exercise3.getId());
                exerciseService.deleteExercise(exercise4.getId());
                workoutService.deleteWorkout(workout.getId());
                gymUserService.deleteUser(gymUser.getId());
            }
        } catch (Exception e) {
            System.err.println("Error during workout exercise tests: " + e.getMessage());
        } finally {
            System.out.println("\n========== WORKOUT EXERCISE TESTS END ==========\n");
        }

        // Workout Service test
        try {
            System.out.println("\n========== WORKOUT TESTS ==========\n");
            GymUserService gymUserService = new GymUserService();
            GymUser gymUser = new GymUser(null, "John Doe", 28, 1.85, 90.0);
            gymUserService.createUser(gymUser);
            WorkoutService workoutService = new WorkoutService();
            Workout workout = new Workout(null, "Full Body Workout", gymUser.getId(), WorkoutStatus.PLANNED, LocalDateTime.now());

            System.out.println("Creating workout....");
            workoutService.createWorkout(workout);
            workoutService.findAll().forEach(System.out::println);

            System.out.println("Updating workout....");
            workout.setName("Updated Full Body Workout");
            workoutService.updateWorkout(workout);
            workoutService.findAll().forEach(System.out::println);

            System.out.println("Finding by id....");
            Workout findById = workoutService.findById(workout.getId());
            System.out.println(findById);

            System.out.println("Finding completed workouts....");
            List<Workout> completedWorkouts = workoutService.findCompletedWorkouts();
            if (completedWorkouts.isEmpty()) {
                System.out.println("No completed workouts found");
            } else {
                System.out.println("Completed workouts found:");
                completedWorkouts.forEach(System.out::println);
            }

            System.out.println("Finding planned workouts....");
            List<Workout> plannedWorkouts = workoutService.findPlannedWorkouts();
            if (plannedWorkouts.isEmpty()) {
                System.out.println("No planned workouts found");
            } else {
                System.out.println("Planned workouts found:");
                plannedWorkouts.forEach(System.out::println);
            }

            System.out.println("Finding canceled workouts....");
            List<Workout> canceledWorkouts = workoutService.findCanceledWorkouts();
            if (canceledWorkouts.isEmpty()) {
                System.out.println("No canceled workouts found");
            } else {
                System.out.println("Canceled workouts found:");
                canceledWorkouts.forEach(System.out::println);
            }

            System.out.println("Finding workouts by user....");
            workoutService.findWorkoutByUser(gymUser.getId()).forEach(System.out::println);

            System.out.println("Sorting workouts by date....");
            workoutService.sortWorkoutsByDate().forEach(System.out::println);

            System.out.println("\nDeleting workouts by user....");
            workoutService.deleteWorkout(workout.getId());
            gymUserService.deleteUser(gymUser.getId());
            workoutService.findAll().forEach(System.out::println);

            System.out.println("Workout test ok ✅");
        } catch (Exception e) {
            System.err.println("Error during workout tests: " + e.getMessage());
        } finally {
            System.out.println("\n========== SERVICE TESTS END ==========\n");
            // Successfully test
        }
    }
}
