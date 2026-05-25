package com.tuon.db;

import com.tuon.entities.Workout;

import java.util.List;

public interface WorkoutDAO {

    void insert(Workout workout);

    void update(Workout workout);

    void deleteById(Integer id);

    Workout findById(Integer id);

    List<Workout> findAll();

}
