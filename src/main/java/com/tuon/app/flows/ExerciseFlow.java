package com.tuon.app.flows;

import com.tuon.entities.Exercise;
import com.tuon.enums.Difficulty;
import com.tuon.exceptions.FlowExecption;
import com.tuon.services.ExerciseService;

import java.util.Scanner;

public class ExerciseFlow {

    private final Scanner sc;
    private final ExerciseService exerciseService;

    public ExerciseFlow() {
        this.sc = new Scanner(System.in);
        this.exerciseService = new ExerciseService();
    }

    public void createExercise() {
        int exercisesNumber = readInt("Enter number of exercises to be created: ");

        for (int i = 0; i < exercisesNumber; i++) {
            String name = readString("Enter exercise name: ");
            String muscleGroup = readString("Enter muscle group: ");
            String difficulty = readString("Enter difficulty (EASY/MEDIUM/HARD): ");
            Difficulty exerciseDifficulty;
            try {
                exerciseDifficulty = Difficulty.valueOf(difficulty.trim().toUpperCase());
            } catch (IllegalArgumentException iae) {
                System.out.println("Invalid role. Use one of: TRAINER, RECEPTIONIST, MANAGER");
                return;
            }
            Exercise exercise = new Exercise(null, name, muscleGroup, exerciseDifficulty);
            System.out.println(exercise);

            String firstUpdate = readString("Do you want to change some information about the exercise? (yes/no)");
            if (firstUpdate.equalsIgnoreCase("yes")) {
                boolean editing = true;
                while (editing) {
                    int changeChoice = readInt("What would you like to change? \n1.name \n2. muscle group \n3. difficulty\n0. exit");

                    switch (changeChoice) {
                        case 1:
                            String newName = readString("Enter the new name: ");
                            exercise.setName(newName);
                            System.out.println("New name has been changed to " + newName);
                            break;
                        case 2:
                            String newMuscleGroup = readString("Enter the new muscle group: ");
                            exercise.setMuscleGroup(newMuscleGroup);
                            System.out.println("New muscle group has been changed to " + newMuscleGroup);
                            break;
                        case 3:
                            String newDifficulty = readString("Enter the new difficulty (EASY/MEDIUM/HARD): ");
                            Difficulty updatedDifficulty;
                            try {
                                updatedDifficulty = Difficulty.valueOf(newDifficulty.trim().toUpperCase());
                                exercise.setDifficulty(updatedDifficulty);
                                System.out.println("New difficulty has been changed to " + updatedDifficulty);
                            } catch (IllegalArgumentException iae) {
                                System.out.println("Invalid difficulty. Use one of: EASY, MEDIUM, HARD");
                            }
                            break;
                        case 0:
                            editing = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
            try {
                exerciseService.createExercise(exercise);
                System.out.println("Exercise created successfully!");
            } catch (FlowExecption e) {
                System.out.println("Error creating exercise: " + e.getMessage());
            }
        }
        System.out.println("Exercise(s) created successfully.");
        exerciseService.findAll().forEach(System.out::println);
    }

    public void updateExercise() {
        int getId = readInt("Type the exercise ID for update: ");
        Exercise exercise = findExerciseOrNull(getId);
        if (exercise == null) {
            return;
        }
        System.out.println("Exercise found:");
        System.out.println(exercise);

        boolean editing = true;
        boolean hasChanged = false;
        while (editing) {
            int choice = readInt("What would you like to update? \n1.name \n2. muscle group \n3.difficulty \n0. exit");
            switch (choice) {
                case 1:
                    String newName = readString("Enter the new name: ");
                    exercise.setName(newName);
                    hasChanged = true;
                    System.out.println("New name has been changed to " + newName);
                    break;
                case 2:
                    String newMuscleGroup = readString("Enter the new muscle group: ");
                    exercise.setMuscleGroup(newMuscleGroup);
                    hasChanged = true;
                    System.out.println("New muscle group has been changed to " + newMuscleGroup);
                    break;
                case 3:
                    String newDifficulty = readString("Enter the new difficulty (EASY/MEDIUM/HARD): ");
                    Difficulty updatedDifficulty;
                    try {
                        updatedDifficulty = Difficulty.valueOf(newDifficulty.trim().toUpperCase());
                        exercise.setDifficulty(updatedDifficulty);
                        hasChanged = true;
                        System.out.println("New difficulty has been changed to " + updatedDifficulty);
                    } catch (IllegalArgumentException iae) {
                        System.out.println("Invalid difficulty. Use one of: EASY, MEDIUM, HARD");
                    }
                    break;
                case 0:
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        try {
            if (hasChanged) {
                exerciseService.updateExercise(exercise);
                System.out.println("Exercise updated successfully!");
                hasChanged = false;
            }
        } catch (FlowExecption e) {
            System.out.println("Error updating exercise: " + e.getMessage());
        }
    }

    public void findAllExercises() {
        if (exerciseService.findAll().isEmpty()) {
            System.out.println("No exercises in our database!");
            return;
        }
        System.out.println("All exercises in our database:");
        exerciseService.findAll().forEach(System.out::println);
    }

    public void findExerciseById() {
        int id = readInt("Type the exercise ID: ");
        Exercise exercise = findExerciseOrNull(id);
        if (exercise == null) {
            System.out.println("Exercise not found.");
            return;
        }
        try {
            System.out.println("Exercise found:");
            System.out.println(exercise);
        } catch (FlowExecption e) {
            System.out.println("Error finding exercise: " + e.getMessage());
        }
    }

    public void findExerciseByDifficultyAndMuscleGroup() {
        if (exerciseService.findAll().isEmpty()) {
            System.out.println("No exercises in our database!");
            return;
        }
        String chosenDifficulty = readString("Enter the difficulty (EASY/MEDIUM/HARD): ");
        String chosenMuscleGroup = readString("Enter the muscle group: ");
        exerciseService.findByDifficultyAndMuscleGroup(Difficulty.valueOf(chosenDifficulty.trim().toUpperCase()), chosenMuscleGroup);
    }

    public void findExerciseByBestCombination() {
        if (exerciseService.findAll().isEmpty()) {
            System.out.println("No exercises in our database!");
            return;
        }
        String chosenDifficulty = readString("Enter the difficulty (EASY/MEDIUM/HARD): ");
        String chosenMuscleGroup = readString("Enter the muscle group: ");
        exerciseService.bestCombination(Difficulty.valueOf(chosenDifficulty.trim().toUpperCase()), chosenMuscleGroup);
    }

    public void deleteExercise() {
        if (exerciseService.findAll().isEmpty()) {
            System.out.println("No exercises in our database!");
        }
        int getId = readInt("Enter the exercise ID to delete: ");
        Exercise exercise = findExerciseOrNull(getId);
        if (exercise == null) {
            System.out.println("Exercise not found.");
            return;
        }
        System.out.println("Exercise to be deleted:");
        System.out.println(exercise);
        String confirm = readString("\nDo you really want to delete this exercise? (yes/no)");
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Delete cancelled.");
            return;
        }
        exerciseService.deleteExercise(getId);
        System.out.println("Exercise deleted successfully!");
    }

    private int readInt(String message) {
        while (true) {
            try {
                System.out.println(message);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid value");
            }
        }
    }

    private String readString(String message) {
        while (true) {
            System.out.println(message);
            String value = sc.nextLine();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Invalid input");
        }
    }

    private Exercise findExerciseOrNull(Integer id) {
        Exercise exercise = exerciseService.findById(id);
        if (exercise == null) {
            System.out.println("Exercise not found");
        }
        return exercise;
    }


}


