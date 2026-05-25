package com.tuon.entities;

import com.tuon.enums.WorkoutStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Workout {

    private Integer id;
    private String name;
    private Integer userId;
    private WorkoutStatus status = WorkoutStatus.PLANNED;
    private LocalDateTime date = LocalDateTime.now();
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();

    public Workout() {

    }

    public Workout(Integer id, String name, Integer userId, WorkoutStatus status, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.status = status;
        this.date = date;
    }

    // getters e setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public WorkoutStatus getStatus(){
        return status;
    }

    public void setStatus (WorkoutStatus status){
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void start() {
        this.status = WorkoutStatus.IN_PROGRESS;
    }

    public void complete() {
        this.status = WorkoutStatus.COMPLETED;
    }

    public void cancel() {
        this.status = WorkoutStatus.CANCELED;
    }

    public double calculateTotalVolume() {
        return workoutExercises.stream()
                .mapToDouble(we -> we.calculateVolume())
                .sum();
    }

    public List<WorkoutExercise> getWorkoutExercises() {
        return workoutExercises;
    }

    public void setWorkoutExercises(List<WorkoutExercise> workoutExercises) { this.workoutExercises = workoutExercises;}

    // comportamento da entidade
    public void addWorkoutExercise(WorkoutExercise we) {
        this.workoutExercises.add(we);
    }

    public void removeWorkoutExercise(WorkoutExercise we) {
        this.workoutExercises.remove(we);
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", status=" + status +
                ", date=" + date +
                ", totalExercises=" + workoutExercises.size() +
                ", totalVolume=" + calculateTotalVolume() +
                '}';
    }

}
