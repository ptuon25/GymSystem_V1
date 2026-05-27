package com.tuon.services;

import com.tuon.db.DAO.ExerciseDAO;
import com.tuon.db.DAO.WorkoutExerciseDAO;
import com.tuon.db.DAOImpl.ExerciseDAOImpl;
import com.tuon.db.DAOImpl.WorkoutExerciseDAOImpl;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.WorkoutExercise;
import com.tuon.exceptions.ServiceException;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

public class WorkoutExerciseService {

    public WorkoutExercise createWorkoutExercise(WorkoutExercise workoutExercise) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutExerciseDAO workoutExerciseDAO = new WorkoutExerciseDAOImpl(conn);
            validateForCreate(workoutExercise);
            workoutExerciseDAO.insert(workoutExercise);
            return workoutExercise;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public void validateForCreate(WorkoutExercise workoutExercise) {

        if (workoutExercise == null) {
            throw new ServiceException("WorkoutExercise cannot be null");
        }
        if (workoutExercise.getExercise() == null) {
            throw new ServiceException("WorkoutExercise exercise cannot be null");
        }
        if (workoutExercise.getWorkoutId() == null) {
            throw new ServiceException("WorkoutExercise workoutId cannot be null");
        }
        if (workoutExercise.getReps() == null || workoutExercise.getReps() <= 0) {
            throw new ServiceException("WorkoutExercise reps must be greater than 0");
        }
        if (workoutExercise.getSets() == null || workoutExercise.getSets() <= 0) {
            throw new ServiceException("WorkoutExercise sets must be greater than 0");
        }
        if (workoutExercise.getWeight() == null || workoutExercise.getWeight() < 0) {
            throw new ServiceException("WorkoutExercise weight must be 0 or greater");
        }
        if (workoutExercise.getRestSeconds() == null || workoutExercise.getRestSeconds() <= 0) {
            throw new ServiceException("WorkoutExercise restSeconds must be greater than 0");
        }
        if (workoutExercise.getPosition() == null || workoutExercise.getPosition() <= 0) {
            throw new ServiceException("WorkoutExercise position must be greater than 0");
        }
    }

    public WorkoutExercise updateWorkoutExercise(WorkoutExercise workoutExercise) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutExerciseDAO workoutExerciseDAO = new WorkoutExerciseDAOImpl(conn);
            validadeForUpdate(workoutExercise);
            workoutExerciseDAO.update(workoutExercise);
            return workoutExercise;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public void validadeForUpdate(WorkoutExercise workoutExercise) {
        if (workoutExercise == null) {
            throw new ServiceException("WorkoutExercise cannot be null");
        }
        if (workoutExercise.getWorkoutId() == null || workoutExercise.getWorkoutId() <= 0) {
            throw new ServiceException("WorkoutExercise workoutId cannot be null or less than or equal to 0");
        }
        if (workoutExercise.getExercise() == null || workoutExercise.getExercise().getId() == null || workoutExercise.getExercise().getId() <= 0) {
            throw new ServiceException("WorkoutExercise exercise cannot be null and must have a valid id");
        }
    }

    public void deleteWorkoutExercise(Integer workoutId, Integer exerciseId) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutExerciseDAO workoutExerciseDAO = new WorkoutExerciseDAOImpl(conn);
            if (workoutId == null || workoutId <= 0) {
                throw new ServiceException("WorkoutExercise workoutId cannot be null or less than or equal to 0");
            }
            if (exerciseId == null || exerciseId <= 0) {
                throw new ServiceException("WorkoutExercise exerciseId cannot be null or less than or equal to 0");
            }
            workoutExerciseDAO.deleteByWorkoutAndExercise(workoutId, exerciseId);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();

        }
    }

    public WorkoutExercise findByWorkoutAndExercise(Integer workoutId, Integer exerciseId) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutExerciseDAO workoutExerciseDAO = new WorkoutExerciseDAOImpl(conn);
            if (workoutId == null || workoutId <= 0) {
                throw new ServiceException("WorkoutExercise workoutId cannot be null or less than or equal to 0");
            }
            if (exerciseId == null || exerciseId <= 0) {
                throw new ServiceException("WorkoutExercise exerciseId cannot be null or less than or equal to 0");
            }
            return workoutExerciseDAO.findByWorkoutAndExercise(workoutId, exerciseId);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<WorkoutExercise> findAll() {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutExerciseDAO workoutExerciseDAO = new WorkoutExerciseDAOImpl(conn);
            return workoutExerciseDAO.findAll().stream().sorted(Comparator.comparing(WorkoutExercise::getWorkoutId).thenComparing(we -> we.getExercise().getId())).toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }

    }

    public List<WorkoutExercise> findAllInvalidExercises() {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutExerciseDAO workoutExerciseDAO = new WorkoutExerciseDAOImpl(conn);
            return workoutExerciseDAO.findAll().stream().filter(we -> we.getExercise() != null).toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public void batchInsert(List<WorkoutExercise> list) {
        try {
            Connection conn = DbConnection.getConnection();
            WorkoutExerciseDAO dao = new WorkoutExerciseDAOImpl(conn);
            dao.batchInsert(list);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

}
