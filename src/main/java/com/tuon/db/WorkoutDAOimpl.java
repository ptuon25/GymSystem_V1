package com.tuon.db;

import com.tuon.entities.Exercise;
import com.tuon.entities.Workout;
import com.tuon.entities.WorkoutExercise;
import com.tuon.enums.Difficulty;
import com.tuon.enums.WorkoutStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDAOimpl implements WorkoutDAO {

    private final Connection conn;

    public WorkoutDAOimpl(Connection conn) {
        this.conn = conn;
    }

    private void saveWorkoutExercises(Workout workout) {
        String sql = "INSERT INTO workout_exercises (workout_id, exercise_id, steps, reps, weight, rest_seconds, position) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql);
            for (WorkoutExercise we : workout.getWorkoutExercises()) {
                st.setInt(1, workout.getId());
                st.setInt(2, we.getExercise().getId());
                st.setInt(3, we.getSteps());
                st.setInt(4, we.getReps());
                st.setDouble(5, we.getWeight());
                st.setInt(6, we.getRestSeconds());
                st.setInt(7, we.getPosition());
                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void insert(Workout workout) {
        String sql = "INSERT INTO workouts (user_id, name, status, date) VALUES (?, ?, ?, ?)";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setInt(1, workout.getUserId());
            st.setString(2, workout.getName());
            st.setString(3, workout.getStatus().toString());
            st.setObject(4, workout.getDate(), Types.TIMESTAMP);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    workout.setId(id);
                    if (!workout.getWorkoutExercises().isEmpty()) {
                        saveWorkoutExercises(workout);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
            DbConnection.closeResultSet(rs);
        }
    }

    @Override
    public void update(Workout workout) {
        String sql = "UPDATE workouts SET name = ?, status = ?, date = ? WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, workout.getName());
            st.setString(2, workout.getStatus().toString());
            st.setObject(3, workout.getDate(), Types.TIMESTAMP);
            st.setInt(4, workout.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        // Deletar exercícios antigos e salvar novos
        String deleteSql = "DELETE FROM workout_exercises WHERE workout_id = ?";
        try (PreparedStatement st = conn.prepareStatement(deleteSql)) {
            st.setInt(1, workout.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        if (!workout.getWorkoutExercises().isEmpty()) {
            saveWorkoutExercises(workout);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            // Deletar exercícios primeiro
            st = conn.prepareStatement("DELETE FROM workout_exercises WHERE workout_id = ?");
            st.setInt(1, id);
            st.executeUpdate();
            DbConnection.closeStatement(st);

            // Depois deletar treino
            st = conn.prepareStatement("DELETE FROM workouts WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public Workout findById(Integer id) {
        String sql = "SELECT id, user_id, name, status, date FROM workouts WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Workout workout = instantiateWorkout(rs);
                workout.setWorkoutExercises(findWorkoutExercisesByWorkoutId(workout.getId()));
                return workout;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public List<Workout> findAll() {
        String sql = "SELECT id, user_id, name, status, date FROM workouts ORDER BY date DESC";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            List<Workout> list = new ArrayList<>();
            while (rs.next()) {
                list.add(instantiateWorkout(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    private Workout instantiateWorkout(ResultSet rs) throws SQLException {
        Workout workout = new Workout();
        workout.setId(rs.getInt("id"));
        workout.setUserId(rs.getInt("user_id"));
        workout.setName(rs.getString("name"));
        workout.setStatus(WorkoutStatus.valueOf(rs.getString("status")));
        workout.setDate(rs.getObject("date", LocalDateTime.class));
        return workout;
    }

    private List<WorkoutExercise> findWorkoutExercisesByWorkoutId(Integer workoutId) {
        String sql = "SELECT we.workout_id, we.exercise_id, we.steps, we.reps, we.weight, we.rest_seconds, we.position, " +
                "e.id, e.name, e.muscle_group, e.difficulty " +
                "FROM workout_exercises we " +
                "JOIN exercises e ON we.exercise_id = e.id " +
                "WHERE we.workout_id = ? " +
                "ORDER BY we.position";
        PreparedStatement st = null;
        ResultSet rs = null;
        List<WorkoutExercise> list = new ArrayList<>();
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, workoutId);
            rs = st.executeQuery();

            while (rs.next()) {
                WorkoutExercise we = new WorkoutExercise();
                we.setWorkoutId(rs.getInt("workout_id"));

                Exercise exercise = new Exercise();
                exercise.setId(rs.getInt("exercise_id"));
                exercise.setName(rs.getString("name"));
                exercise.setMuscleGroup(rs.getString("muscle_group"));
                exercise.setDifficulty(Difficulty.valueOf(rs.getString("difficulty")));
                we.setExercise(exercise);

                we.setSteps(rs.getInt("steps"));
                we.setReps(rs.getInt("reps"));
                we.setWeight(rs.getDouble("weight"));
                we.setRestSeconds(rs.getInt("rest_seconds"));
                we.setPosition(rs.getInt("position"));
                list.add(we);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }
}