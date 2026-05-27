package com.tuon.db.DAO;

import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Workout;
import com.tuon.entities.WorkoutExercise;
import com.tuon.exceptions.DbException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface WorkoutExerciseDAO {

    void insert(WorkoutExercise workoutExercise);

    void update(WorkoutExercise workoutExercise);

    void deleteByWorkoutAndExercise(Integer WorkoutId, Integer exerciseId);

    void deleteByWorkoutId(Integer workoutID);

    WorkoutExercise findByWorkoutAndExercise(Integer workoutId, Integer exerciseId);

    List<WorkoutExercise> findByWorkoutId(Integer workoutId);

    List<WorkoutExercise> findAll();

    void batchInsert(List<WorkoutExercise> list);

}
