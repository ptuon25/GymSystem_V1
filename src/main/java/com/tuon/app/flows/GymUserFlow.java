package com.tuon.app.flows;

import com.tuon.entities.GymUser;
import com.tuon.exceptions.FlowExecption;
import com.tuon.exceptions.ServiceException;
import com.tuon.services.GymUserService;

import javax.sql.rowset.serial.SerialException;
import java.util.Scanner;

public class GymUserFlow {

    private final Scanner sc;
    private final GymUserService gymUserService;

    public GymUserFlow() {
        this.sc = new Scanner(System.in);
        this.gymUserService = new GymUserService();
    }

    public void createGymUser() {

        int studentsNumber = readInt("How many students do you want to create?");

        for (int i = 0; i < studentsNumber; i++) {
            String name = readString("Enter the name: ");
            int age = readInt("Enter the age: ");
            Double height = readDouble("Enter the height: ");
            Double weight = readDouble("Enter the weight: ");

            GymUser gymUser = new GymUser(null, name, age, height, weight);
            System.out.println(gymUser);

            String choice = readString("Do you want to change some information about the user? (yes/no)");

            if (choice.equalsIgnoreCase("yes")) {
                boolean editing = true;
                while (editing) {
                    int changeChoice = readInt("What would you like to change? \n1.name \n2. age \n3. height\n 4. weight \n0. exit");

                    switch (changeChoice) {
                        case 1:
                            String newName = readString("Enter the new name: ");
                            gymUser.setName(newName);
                            System.out.println("New name has been changed to " + newName);
                            break;
                        case 2:
                            int newAge = readInt("Enter the new age: ");
                            gymUser.setAge(newAge);
                            System.out.println("New age has been changed to " + newAge);
                            break;
                        case 3:
                            double newHeight = readDouble("Enter the new height: ");
                            gymUser.setHeight(newHeight);
                            System.out.println("New height has been changed to " + newHeight);
                            break;
                        case 4:
                            double newWeight = readDouble("Enter the new weight: ");
                            gymUser.setWeight(newWeight);
                            System.out.println("New weight has been changed to " + newWeight);
                            break;
                        case 0:
                            System.out.println("Exiting....");
                            editing = false;
                            break;
                    }
                    System.out.println(gymUser);
                }
            }
            try {
                gymUserService.createUser(gymUser);
                System.out.println("User " + gymUser.getName() + " has been created");
            } catch (FlowExecption e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Gym user(s) created: ");
        gymUserService.findAll().forEach(System.out::println);
    }

    public void updateGymUser() {

        int getId = readInt("Type the gym user ID for update: ");
        GymUser gymUser = findUserOrNull(getId);
        if (gymUser == null) {
            return;
        }
        System.out.println("User found: ");
        System.out.println(gymUser);

        boolean editing = true;
        boolean hasChanges = false;
        while (editing) {
            int choice = readInt("What would you like to update? \n1.name \n2. age \n3. height \n4. weight \n0. exit");
            switch (choice) {
                case 1:
                    System.out.println("Current name: " + gymUser.getName());
                    String name = readString("Enter the new name: ");
                    gymUser.setName(name);
                    hasChanges = true;
                    System.out.println("New name has been changed to " + name);
                    break;
                case 2:
                    System.out.println("Current age: " + gymUser.getAge());
                    int age = readInt("Enter the new age: ");
                    gymUser.setAge(age);
                    hasChanges = true;
                    System.out.println("New age has been changed to " + age);
                    break;
                case 3:
                    System.out.println("Current height: " + gymUser.getHeight());
                    double height = readDouble("Enter the new height: ");
                    gymUser.setHeight(height);
                    hasChanges = true;
                    System.out.println("New height has been changed to " + height);
                    break;
                case 4:
                    System.out.println("Current weight: " + gymUser.getWeight());
                    double weight = readDouble("Enter the new weight: ");
                    gymUser.setWeight(weight);
                    hasChanges = true;
                    System.out.println("New weight has been changed to " + weight);
                    break;
                case 0:
                    System.out.println("Exiting....");
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
        try {
            if (hasChanges) {
                gymUserService.updateUser(gymUser);
                System.out.println("User " + gymUser.getName() + " has been updated");
                hasChanges = false;
            }
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findUserById() {
        int getId = readInt("Type the gym user ID to find: ");
        GymUser gymUser = findUserOrNull(getId);
        if (gymUser == null) {
            return;
        }
        try {
            System.out.println("User found: ");
            System.out.println(gymUserService.findById(getId));
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void findAllUsers() {
        if (gymUserService.findAll().isEmpty()) {
            System.out.println("There are no users found.");
            return;
        }
        System.out.println("All users in database: ");
        gymUserService.findAll().forEach(System.out::println);
    }

    public void findInvalidUsers() {
        if (gymUserService.findInvalidUsers().isEmpty()) {
            System.out.println("There are no invalid users found.");
            return;
        }
        System.out.println("All invalid users in database: ");
        gymUserService.findInvalidUsers().forEach(System.out::println);
    }

    public void getUsersOrderedByBMI() {
        System.out.println("Users ordered by bmi: ");
        gymUserService.getUsersOrderedByBMI().forEach(System.out::println);
    }

    public void deleteUser() {
        int getId = readInt("Type the gym user ID to delete: ");
        GymUser gymUser = findUserOrNull(getId);
        if (gymUser == null) {
            return;
        }
        System.out.println("User found: ");
        System.out.println(gymUser);

        boolean editing = true;
        while (editing) {
            String choice = readString("Are you sure you want to delete this user? (yes/no)");
            if (choice.equalsIgnoreCase("yes")) {
                try {
                    System.out.println("Deleting user " + gymUser.getName());
                    gymUserService.deleteUser(getId);
                } catch (FlowExecption e) {
                    System.out.println(e.getMessage());
                }
                editing = false;
            } else if (choice.equalsIgnoreCase("no")) {
                System.out.println("Exiting....");
                editing = false;
            } else {
                System.out.println("Invalid option");
            }
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

    private GymUser findUserOrNull(Integer id) {
        GymUser user = gymUserService.findById(id);
        if (user == null) {
            System.out.println("User not found");
        }
        return user;
    }
}