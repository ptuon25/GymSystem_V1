package com.tuon.services;

import com.tuon.db.DAO.WorkoutDAO;
import com.tuon.db.DAOImpl.WorkoutDAOImpl;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Workout;
import com.tuon.enums.WorkoutStatus;
import com.tuon.exceptions.ServiceException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WorkoutService {

    public Workout createWorkout(Workout workout) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            validateForCreate(workout);
            workoutDAO.insert(workout);
            return workout;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }finally {
            DbConnection.closeConnection();
        }
    }

    private void validateForCreate(Workout workout) {

        if (workout == null) {
            throw new ServiceException("Workout cannot be null");
        }
        if (workout.getId() == null) {
            throw new ServiceException("Workout id cannot be null");
        }
        if (workout.getName() == null || workout.getName().isEmpty()) {
            throw new ServiceException("Workout name cannot be null or empty");
        }
        if (workout.getUserId() == null || workout.getUserId() <= 0) {
            throw new ServiceException("Workout userId must be greater than 0");
        }
        if (workout.getStatus() == null) {
            throw new ServiceException("Workout status must be PLANNED, COMPLETED or CANCELED");
        }
        if (workout.getStatus() != WorkoutStatus.PLANNED) {
            throw new ServiceException("Workout status must start as PLANNED");
        }
        if (workout.getDate() == null) {
            throw new ServiceException("Workout date cannot be null");
        }
        if (workout.getDate().isBefore(LocalDateTime.now())) {
            throw new ServiceException("Workout date cannot be in the past");
        }
        if (workout.getWorkoutExercises() == null || workout.getWorkoutExercises().isEmpty()) {
            throw new ServiceException("Workout exercises cannot be null or empty");
        }
    }

    public Workout updateWorkout(Workout workout) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            validateForUpdate(workout);
            workoutDAO.update(workout);
            return workout;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    private void validateForUpdate(Workout workout) {
        if (workout == null) {
            throw new ServiceException("Workout cannot be null");
        }
        if (workout.getId() == null || workout.getId() <= 0) {
            throw new ServiceException("Workout id must be greater than 0");
        }
        if (workout.getName() == null || workout.getName().isEmpty()) {
            throw new ServiceException("Workout name cannot be null or empty");
        }
        if (workout.getStatus() == null) {
            throw new ServiceException("Workout status cannot be null");
        }
        if (workout.getDate() == null) {
            throw new ServiceException("Workout date cannot be null");
        }
        if (workout.getDate().isBefore(LocalDateTime.now())) {
            throw new ServiceException("Workout date cannot be in the past");
        }
        if (workout.getWorkoutExercises() == null || workout.getWorkoutExercises().isEmpty()) {
            throw new ServiceException("Workout exercises cannot be empty");
        }
    }

    public void deleteWorkout(Integer workoutId) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            if (workoutId == null || workoutId <= 0) {
                throw new ServiceException("Workout id must be greater than 0");
            }
            workoutDAO.deleteById(workoutId);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public Workout findById(Integer workoutId) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            if (workoutId == null || workoutId <= 0) {
                throw new ServiceException("Workout id must be greater than 0");
            }
            return workoutDAO.findById(workoutId);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<Workout> findAll() {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            return workoutDAO.findAll().stream().filter(workout -> workout.getStatus() != WorkoutStatus.CANCELED).toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<Workout> findCompletedWorkouts() {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            return workoutDAO.findAll().stream().filter(workout -> workout.getStatus() == WorkoutStatus.COMPLETED).collect(toList());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<Workout> findPlannedWorkouts() {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            return workoutDAO.findAll().stream().filter(workout -> workout.getStatus() == WorkoutStatus.PLANNED).collect(toList());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<Workout> findCanceledWorkouts() {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            return workoutDAO.findAll().stream().filter(workout -> workout.getStatus() == WorkoutStatus.CANCELED).collect(toList());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<Workout> findWorkoutByUser(Integer userId) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            if (userId == null || userId <= 0) {
                throw new ServiceException("Workout userId must be greater than 0");
            }
            return workoutDAO.findAll().stream().filter(workout -> workout.getUserId().equals(userId)).collect(toList());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();

        }
    }

    public List<Workout> sortWorkoutsByDate() {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutDAO workoutDAO = new WorkoutDAOImpl(conn);
            return workoutDAO.findAll().stream().sorted((w1, w2) -> w1.getDate().compareTo(w2.getDate())).collect(toList());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

}



