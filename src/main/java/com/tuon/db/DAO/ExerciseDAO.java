package com.tuon.db.DAO;

import com.tuon.entities.Exercise;

import java.util.List;

public interface ExerciseDAO {

    void insert(Exercise exercise);

    void update(Exercise exercise);

    void deleteById(Integer id);

    Exercise findById(Integer id);

    List<Exercise> findAll();

    List<Exercise> findExercisesByWorkoutId(Integer id);

}
