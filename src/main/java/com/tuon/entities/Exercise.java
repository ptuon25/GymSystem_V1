// classe do exercício físico cadastrado no sistema

package com.tuon.entities;

import com.tuon.enums.Difficulty;

public class Exercise {

    private Integer id;
    private String name;
    private String muscleGroup;
    private Difficulty difficulty;

    public Exercise(){

    }

    public Exercise(Integer id, String name, String muscleGroup, Difficulty dificulty) {
        this.id = id;
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.difficulty = dificulty;
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

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "Exercise{ " +
                "id='" + id + '\'' +
                ", name=' " + name + '\'' +
                ", muscleGroup=' " + muscleGroup + '\'' +
                ", dificulty= " + difficulty +
                '}';
    }
}
