package com.tuon.services;

import com.tuon.db.DAO.ExerciseDAO;
import com.tuon.db.DAOImpl.ExerciseDAOImpl;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Exercise;
import com.tuon.exceptions.ServiceException;

import java.sql.Connection;
import java.util.List;

public class ExerciseService {

    public Exercise createExercise(Exercise exercise) {
        try {
            Connection conn = DbConnection.getConnection();
            ExerciseDAO exerciseDAO = new ExerciseDAOImpl(conn);
            validateForCreate(exercise);
            exerciseDAO.insert(exercise);
            return exercise;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    private void validateForCreate(Exercise exercise) {

        if (exercise == null) {
            throw new ServiceException("Exercise cannot be null");
        }
        if (exercise.getId() != null) {
            throw new ServiceException("Exercise id must be null");
        }
        if (exercise.getName() == null || exercise.getName().isEmpty()) {
            throw new ServiceException("Exercise name cannot be null or empty");
        }
        if (exercise.getMuscleGroup() == null || exercise.getMuscleGroup().isEmpty()) {
            throw new ServiceException("Exercise muscle group cannot be null or empty");
        }
        if (exercise.getDifficulty() == null) {
            throw new ServiceException("Exercise difficulty cannot be null");
        }
    }

    public Exercise updateExercise(Exercise exercise) {
        try {
            Connection conn = DbConnection.getConnection();
            ExerciseDAO exerciseDAO = new ExerciseDAOImpl(conn);
            validateForUpdate(exercise);
            exerciseDAO.update(exercise);
            return exercise;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    private void validateForUpdate(Exercise exercise) {
        if (exercise == null) {
            throw new ServiceException("Exercise cannot be null");
        }
        if (exercise.getId() == null || exercise.getId() <= 0) {
            throw new ServiceException("Exercise id cannot be null");
        }
        if (exercise.getName() == null || exercise.getName().isEmpty()) {
            throw new ServiceException("Exercise name cannot be null or empty");
        }
        if (exercise.getMuscleGroup() == null || exercise.getMuscleGroup().isEmpty()) {
            throw new ServiceException("Exercise muscle group cannot be null or empty");
        }
        if (exercise.getDifficulty() == null) {
            throw new ServiceException("Exercise difficulty cannot be null");
        }
    }

    public void deleteExercise(Integer id) {
        try {
            Connection conn = DbConnection.getConnection();
            ExerciseDAO exerciseDAO = new ExerciseDAOImpl(conn);
            if (id == null || id <= 0) {
                throw new ServiceException("Exercise id must be greater than 0");
            }
            exerciseDAO.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public Exercise findById(Integer id) {
        try {
            Connection conn = DbConnection.getConnection();
            ExerciseDAO exerciseDAO = new ExerciseDAOImpl(conn);
            if (id == null || id <= 0) {
                throw new ServiceException("Exercise id must be greater than 0");
            }
            return exerciseDAO.findById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<Exercise> findAll() {
        try {
            Connection conn = DbConnection.getConnection();
            ExerciseDAO exerciseDAO = new ExerciseDAOImpl(conn);
            return exerciseDAO.findAll();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    

}