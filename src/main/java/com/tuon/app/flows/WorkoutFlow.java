package com.tuon.app.flows;

import com.tuon.entities.GymUser;
import com.tuon.entities.Workout;
import com.tuon.enums.WorkoutStatus;
import com.tuon.exceptions.ServiceException;
import com.tuon.services.GymUserService;
import com.tuon.services.WorkoutService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WorkoutFlow {

    private final Scanner sc;
    private final WorkoutService workoutService;
    private final GymUserService gymUserService;

    public WorkoutFlow(Scanner sc, WorkoutService workoutService) {
        this.sc = sc;
        this.workoutService = workoutService;
        this.gymUserService = new GymUserService();
    }

    public void createWorkout() {
        int userId = readInt("Type the gym user Id to include a workout: ");
        GymUser gymUserValid = findUserOrNull(userId);
        if (gymUserValid == null) {
            System.out.println("Invalid gym user Id");
            return;
        }
        String workoutName = readString("Enter the name of the workout: ");
        Workout workout = new Workout(null, workoutName, gymUserValid.getId(), WorkoutStatus.PLANNED, LocalDateTime.now());
        System.out.println("Workout created");
        System.out.println(workout);

        String choice = readString("Do you like to change any information? (yes/no)");
        if (choice.equalsIgnoreCase("yes")) {
            boolean editing = true;
            while (editing) {
                int changeChoice = readInt("What information do you want to change? \n1. Workout name \n2. Gym user Id \n3. exit");
                switch (changeChoice) {
                    case 1:
                        String newWorkoutName = readString("Enter the name of the workout: ");
                        workout.setName(newWorkoutName);
                        System.out.println("Workout name: " + workout.getName());
                        break;
                    case 2:
                        int newGymUserId = readInt("Enter new gym user Id: ");
                        GymUser newGymUser = findUserOrNull(newGymUserId);
                        if (newGymUser == null) {
                            System.out.println("Invalid gym user Id");
                            break;
                        }
                        if (newGymUser.getId().equals(gymUserValid.getId())) {
                            System.out.println("This is the actual gym user Id");
                            break;
                        }
                        try {
                            boolean exists = workoutService.findWorkoutByUser(newGymUser.getId()).stream().anyMatch(w -> w.getName() != null && w.getName().trim().equalsIgnoreCase(workout.getName().trim()));
                            if (exists) {
                                System.out.println("Error: this user already has a workout with the same name. Change the workout name or choose another user.");
                                break;
                            }
                        } catch (ServiceException se) {
                            System.out.println("Error checking user's workouts: " + se.getMessage());
                            break;
                        }
                        workout.setUserId(newGymUser.getId());
                        System.out.println("New gym user setted.");
                        break;
                    case 3:
                        System.out.println("Exiting....");
                        editing = false;
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            }
        }
        try {
            Workout created = workoutService.createWorkout(workout);
            System.out.println("Workout created");
            System.out.println(created);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateWorkout() {
        int workoutId = readInt("Enter the workout Id to be updated: ");
        Workout workout = findWorkoutOrNull(workoutId);
        if (workout == null) {
            System.out.println("Invalid workout Id");
            return;
        }
        System.out.println("workout found");
        System.out.println(workout);

        boolean editing = true;
        boolean hasChanged = false;
        while (editing) {
            int choice = readInt("What do you want to update? \n1. Workout name \n2. Gym user Id \n3. exit");
            switch (choice) {
                case 1:
                    String newWorkoutName = readString("Enter the new name of the workout: ");
                    if (newWorkoutName.trim().equalsIgnoreCase(workout.getName().trim())) {
                        System.out.println("The new name is the same as current. No changes made.");
                        break;
                    }
                    // valida duplicata no mesmo usuário
                    try {
                        boolean existsWithSameName = workoutService.findWorkoutByUser(workout.getUserId())
                                .stream()
                                .filter(w -> w.getId() != null && !w.getId().equals(workout.getId())) // excluir o próprio
                                .anyMatch(w -> w.getName() != null && w.getName().trim().equalsIgnoreCase(newWorkoutName.trim()));
                        if (existsWithSameName) {
                            System.out.println("Error: this user already has a workout with that name. Choose another name.");
                            break;
                        }
                    } catch (ServiceException se) {
                        System.out.println("Error checking user's workouts: " + se.getMessage());
                        break;
                    }
                    workout.setName(newWorkoutName);
                    hasChanged = true;
                    System.out.println("Workout name set to: " + workout.getName());
                    break;
                case 2:
                    int newGymUserId = readInt("Enter new gym user Id: ");
                    GymUser newGymUser = findUserOrNull(newGymUserId);
                    if (newGymUser == null) {
                        System.out.println("Invalid gym user Id");
                        break;
                    }
                    if (newGymUser.getId().equals(workout.getUserId())) {
                        System.out.println("This is the actual gym user Id");
                        break;
                    }
                    try {
                        boolean exists = workoutService.findWorkoutByUser(newGymUser.getId()).stream().anyMatch(w -> w.getName() != null && w.getName().trim().equalsIgnoreCase(workout.getName().trim()));
                        if (exists) {
                            System.out.println("Error: this user already has a workout with the same name. Change the workout name or choose another user.");
                            break;
                        }
                    } catch (ServiceException se) {
                        System.out.println("Error checking user's workouts: " + se.getMessage());
                        break;
                    }
                    workout.setUserId(newGymUser.getId());
                    hasChanged = true;
                    System.out.println("New gym user setted.");
                    break;
                case 3:
                    System.out.println("Exiting....");
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
        if (!hasChanged) {
            System.out.println("No changes made. Nothing to update.");
            return;
        }
        try {
            Workout updated = workoutService.updateWorkout(workout);
            System.out.println("Workout updated successfully:");
            System.out.println(updated);
        } catch (ServiceException se) {
            System.out.println("Error updating workout: " + se.getMessage());
        }
    }

    public void findAllWorkouts() {
        if (workoutService.findAll().isEmpty()) {
            System.out.println("There are no workouts found.");
        }
        System.out.println("All workouts in our database: ");
        workoutService.findAll().forEach(System.out::println);
    }

    public void findWorkoutById() {
        int getId = readInt("Enter workout Id: ");
        Workout workout = findWorkoutOrNull(getId);
        if (workout == null) {
            System.out.println("Invalid workout Id");
        }
        try {
            System.out.println("Workout found");
            System.out.println(workoutService.findById(workout.getId()));
        } catch (ServiceException se) {
            System.out.println("Error finding workout: " + se.getMessage());
        }
    }

    public void findCompletedWorkouts() {
        System.out.println("All completed workouts in our database: ");
        workoutService.findCompletedWorkouts().forEach(System.out::println);
    }

    public void findPlannedWorkouts() {
        System.out.println("All planned workouts in our database: ");
        workoutService.findPlannedWorkouts().forEach(System.out::println);
    }

    public void findCanceledWorkouts() {
        System.out.println("All canceled workouts in our database: ");
        workoutService.findCanceledWorkouts().forEach(System.out::println);
    }

    public void findWorkoutByUser() {
        int gUId = readInt("Enter gym user Id: ");
        GymUser gymUser = findUserOrNull(gUId);
        if (gymUser == null) {
            System.out.println("Invalid gym user Id");
            return;
        }
        System.out.println("Workout found");
        System.out.println(workoutService.findWorkoutByUser(gymUser.getId()));
    }

    public void sortWorkoutsByDate() {
        System.out.println("All workouts in our database sorted by data: ");
        workoutService.sortWorkoutsByDate().forEach(System.out::println);
    }

    public void deleteWorkout(){
        int getId = readInt("Enter workout Id: ");
        Workout workout = findWorkoutOrNull(getId);
        if (workout == null) {
            System.out.println("Invalid workout Id");
            return;
        }
        try {
            System.out.println("Workout found");
            System.out.println(workout);
            String choice = readString("Do you really want to delete this workout? (yes/no)");
            if(!choice.equalsIgnoreCase("yes")){
                System.out.println("Exiting....");
                return;
            }
            workoutService.deleteWorkout(getId);
            System.out.println("Workout deleted successfully.");
        } catch (ServiceException se) {
            System.out.println("Error finding workout: " + se.getMessage());
        }
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

    private LocalDateTime readLocalDateTime(String message) {
        while (true) {
            try {
                System.out.println(message);
                String input = sc.nextLine();
                return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Invalid date format. Use: yyyy-MM-dd HH:mm:ss");
            }
        }
    }

    private WorkoutStatus readWorkoutStatus(String message) {
        while (true) {
            try {
                System.out.println(message);
                System.out.println("Available statuses: PLANNED, IN_PROGRESS, COMPLETED, CANCELED");
                String input = sc.nextLine();
                return WorkoutStatus.valueOf(input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Please use one of: PLANNED, IN_PROGRESS, COMPLETED, CANCELED");
            }
        }
    }

    private Workout findWorkoutOrNull(Integer id) {
        Workout workout = workoutService.findById(id);
        if (workout == null) {
            System.out.println("Workout not found");
        }
        return workout;
    }

    private GymUser findUserOrNull(Integer id) {
        GymUser user = gymUserService.findById(id);
        if (user == null) {
            System.out.println("User not found");
        }
        return user;
    }
}