package com.tuon.app.flows;

import com.tuon.entities.Exercise;
import com.tuon.entities.Workout;
import com.tuon.entities.WorkoutExercise;
import com.tuon.exceptions.FlowExecption;
import com.tuon.exceptions.ServiceException;
import com.tuon.services.ExerciseService;
import com.tuon.services.WorkoutExerciseService;
import com.tuon.services.WorkoutService;

import java.util.Scanner;

public class WorkoutExerciseFlow {

    private final Scanner sc;
    private final WorkoutExerciseService workoutExerciseService;
    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    public WorkoutExerciseFlow() {
        this.sc = new Scanner(System.in);
        this.workoutExerciseService = new WorkoutExerciseService();
        this.workoutService = new WorkoutService();
        this.exerciseService = new ExerciseService();
    }

    public void createWorkoutExercise() {
        try {
            int exerciseID = readInt("Enter the exercise ID to insert: ");
            Exercise valideExercise = findExerciseOrNull(exerciseID);
            if (valideExercise == null) {
                System.out.println("Invalid exercise ID");
                return;
            }
            try {
                System.out.println("Exercise found: ");
                System.out.println(valideExercise);
            } catch (ServiceException se) {
                System.out.println("Error finding exercise " + se.getMessage());
            }
            int workoutId = readInt("Enter the workout ID to insert: ");
            Workout validWorkout = findWorkoutOrNull(workoutId);
            if (validWorkout == null) {
                System.out.println("Invalid workout ID");
                return;
            }
            try {
                System.out.println("Workout found: ");
                System.out.println(validWorkout);
            } catch (ServiceException se) {
                System.out.println("Error finding workout " + se.getMessage());
            }
            int sets = readInt("Enter number of sets to insert: ");
            checkInvalidValue(sets);
            int reps = readInt("Enter number of reps to insert: ");
            checkInvalidValue(reps);
            double weight = readDouble("Enter weight to insert: ");
            if (weight < 1) {
                System.out.println("Invalid weight to insert");
            }
            int restSeconds = readInt("Enter the rest seconds: ");
            checkInvalidValue(restSeconds);
            int position = readInt("Enter the exercise position: ");
            checkInvalidValue(position);
            WorkoutExercise workoutExercise = new WorkoutExercise(null, valideExercise, validWorkout.getId(), sets, reps, weight, restSeconds, position);
            System.out.println("Workout exercise created: ");
            System.out.println(workoutExercise);
            String choice = readString("Do you want to change any information? (yes/no)");
            if (choice.equalsIgnoreCase("yes")) {
                boolean editing = true;
                while (editing) {
                    int change = readInt("What do you want to change? \n1. Exercise \n2. Workout Id \n3. Set number \n4. Rep number \n5. Weight \n6. Rest Seconds \n7. Position \n8. Exit");
                    switch (change) {
                        case 1:
                            int newExerciseID = readInt("Enter the new exercise ID: ");
                            Exercise newExercise = findExerciseOrNull(newExerciseID);
                            if (newExercise == null) {
                                System.out.println("Invalid exercise ID");
                                return;
                            }
                            if (newExercise == workoutExercise.getExercise()) {
                                System.out.println("Exercise already exists");
                                return;
                            }
                            System.out.println("Exercise found: ");
                            System.out.println(newExercise);
                            try {
                                workoutExercise.setExercise(newExercise);
                                System.out.println("Exercise updated to " + newExercise.getName());
                            } catch (FlowExecption e) {
                                System.out.println("Error updating exercise " + e.getMessage());
                                break;
                            }
                            break;
                        case 2:
                            int newWorkoutId = readInt("Enter the new workout ID: ");
                            Workout newWorkout = findWorkoutOrNull(newWorkoutId);
                            if (newWorkout == null) {
                                System.out.println("Invalid workout ID");
                                return;
                            }
                            if (newWorkoutId == workoutExercise.getWorkoutId()) {
                                System.out.println("The workout ID is already in use");
                                return;
                            }
                            System.out.println("Workout found: ");
                            System.out.println(newWorkout);
                            try {
                                workoutExercise.setWorkoutId(newWorkout.getId());
                                System.out.println("Workout updated to " + newWorkout.getName());
                            } catch (FlowExecption e) {
                                System.out.println("Error updating workout " + e.getMessage());
                                break;
                            }
                            break;
                        case 3:
                            int newSets = readInt("Enter number of sets to insert: ");
                            checkInvalidValue(newSets);
                            if (newSets == workoutExercise.getSets()) {
                                System.out.println("Number of sets already in use");
                                return;
                            }
                            workoutExercise.setSets(newSets);
                            System.out.println("Sets updated to " + workoutExercise.getSets());
                            break;
                        case 4:
                            int newReps = readInt("Enter number of reps to insert: ");
                            checkInvalidValue(newReps);
                            if (newReps == workoutExercise.getReps()) {
                                System.out.println("Number of reps already in use");
                                return;
                            }
                            workoutExercise.setReps(newReps);
                            System.out.println("Reps updated to " + workoutExercise.getReps());
                            break;
                        case 5:
                            double newWeight = readDouble("Enter the new weight to insert: ");
                            if (newWeight < 0) {
                                System.out.println("Invalid weight");
                                return;
                            }
                            if (newWeight == workoutExercise.getWeight()) {
                                System.out.println("Weight already in use");
                                return;
                            }
                            workoutExercise.setWeight(newWeight);
                            System.out.println("Weight updated to " + workoutExercise.getWeight());
                            break;
                        case 6:
                            int newRestSeconds = readInt("Enter number of rest seconds to insert: ");
                            checkInvalidValue(newRestSeconds);
                            if (newRestSeconds == workoutExercise.getRestSeconds()) {
                                System.out.println("Rest seconds already set");
                                return;
                            }
                            workoutExercise.setRestSeconds(newRestSeconds);
                            System.out.println("Rest seconds updated to " + workoutExercise.getRestSeconds());
                            break;
                        case 7:
                            int newPosition = readInt("Enter the new position to insert: ");
                            checkInvalidValue(newPosition);
                            if (newPosition == workoutExercise.getPosition()) {
                                System.out.println("Position already setted");
                                return;
                            }
                            workoutExercise.setPosition(newPosition);
                            System.out.println("Position updated to " + workoutExercise.getPosition());
                            break;
                        case 8:
                            System.out.println("Exiting....");
                            editing = false;
                            break;
                        default:
                            System.out.println("Enter a valid option");
                            break;
                    }
                }
            }
            try {
                workoutExerciseService.createWorkoutExercise(workoutExercise);
                System.out.println("Workout exercise create successfully");
            } catch (ServiceException se) {
                System.out.println("Error creating workout service: " + se.getMessage());
            }
        } catch (FlowExecption fe) {
            System.out.println(fe.getMessage());
        }
    }

    public void updateWorkoutExercise() {

    }

    public void findByWorkoutAndExercise() {

    }

    public void findAllWorkoutExercise() {

    }

    public void findAllInvalidExercises() {

    }

    public void batchInsert() {

    }

    public void findHeaviestExercises() {

    }

    public void findLongestRestExercises() {

    }

    public void findAllOrdered() {

    }

    public void findExercisesByWorkout() {

    }

    public void deleteWorkoutExercise() {

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

    private Double readDouble(String message) {
        while (true) {
            try {
                System.out.println(message);
                return Double.parseDouble(sc.nextLine().replace(",", "."));
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

    private Workout findWorkoutOrNull(Integer id) {
        Workout workout = workoutService.findById(id);
        if (workout == null) {
            System.out.println("Workout not found");
        }
        return workout;
    }

    private void checkInvalidValue(int value) throws FlowExecption {
        if (value < 1) {
            throw new FlowExecption("Invalid value. Must be >= 1");
        }
    }
}
