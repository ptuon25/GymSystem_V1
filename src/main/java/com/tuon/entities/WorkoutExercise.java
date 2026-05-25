package com.tuon.entities;

public class WorkoutExercise {

    private Integer workoutId;
    private Exercise exercise;

    private Integer steps;
    private Integer reps;
    private Double weight;
    private Integer restSeconds;
    private Integer position;

    //construtor vazio
    public WorkoutExercise() {

    }

    //construtor padrão
    public WorkoutExercise(Exercise exercise, Integer workoutId, Integer steps, Integer reps, Double weight, Integer restSeconds, Integer position) {
        this.exercise = exercise;
        this.workoutId = workoutId;
        this.steps = steps;
        this.reps = reps;
        this.weight = weight;
        this.restSeconds = restSeconds;
        this.position = position;
    }

    //getters e setters
    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Integer getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Integer workoutId) {
        this.workoutId = workoutId;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(Integer restSeconds) {
        this.restSeconds = restSeconds;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    // cálculo de volume
    public double calculateVolume(){
        if (steps == 0 || reps == null || weight == null){
            return 0.0;
        }
        return steps * reps * weight;
    }

    @Override
    public String toString() {
        return "WorkoutExercise{" +
                "exercise=" + (exercise != null ? exercise.getName() : "null") +
                ", steps=" + steps +
                ", reps=" + reps +
                ", weight=" + weight +
                ", restSeconds=" + restSeconds +
                ", position=" + position +
                ", volume=" + calculateVolume() +
                '}';
    }
}
