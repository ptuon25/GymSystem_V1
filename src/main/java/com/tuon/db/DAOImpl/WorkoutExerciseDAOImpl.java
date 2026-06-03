package com.tuon.db.DAOImpl;

import com.tuon.db.DAO.WorkoutExerciseDAO;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Exercise;
import com.tuon.entities.WorkoutExercise;
import com.tuon.enums.Difficulty;
import com.tuon.exceptions.DbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WorkoutExerciseDAOImpl implements WorkoutExerciseDAO {

    private final Connection conn;

    public WorkoutExerciseDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(WorkoutExercise workoutExercise) {

        PreparedStatement st = null;

        try {
            String sql1 = """
                        INSERT INTO workout_exercises
                        (workout_id, exercise_id, sets, reps, weight, rest_seconds, position)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;
            st = conn.prepareStatement(sql1);
            st.setInt(1, workoutExercise.getWorkoutId());
            st.setInt(2, workoutExercise.getExercise().getId());
            st.setInt(3, workoutExercise.getSets());
            st.setInt(4, workoutExercise.getReps());
            st.setDouble(5, workoutExercise.getWeight());
            st.setInt(6, workoutExercise.getRestSeconds());
            st.setInt(7, workoutExercise.getPosition());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void update(WorkoutExercise workoutExercise) {

        String sql2 = "UPDATE workout_exercises SET sets = ?, reps = ?, weight = ?, rest_seconds = ?, position = ? WHERE id = ?";
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(sql2);
            st.setInt(1, workoutExercise.getSets());
            st.setInt(2, workoutExercise.getReps());
            st.setDouble(3, workoutExercise.getWeight());
            st.setInt(4, workoutExercise.getRestSeconds());
            st.setInt(5, workoutExercise.getPosition());
            st.setInt(6, workoutExercise.getId());
            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("No workout exercise found with the provided IDs.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void deleteByWorkoutAndExercise(Integer workoutId, Integer exerciseId) {

        String sql3 = "DELETE FROM workout_exercises WHERE workout_id = ? AND exercise_id = ?";

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql3);
            st.setInt(1, workoutId);
            st.setInt(2, exerciseId);
            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("No workout exercise found");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void deleteByWorkoutId(Integer workoutID) {

        String sql4 = "DELETE FROM workout_exercises WHERE workout_id = ?";

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql4);
            st.setInt(1, workoutID);
            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("No workout exercise found");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public WorkoutExercise findByWorkoutAndExercise(Integer workoutId, Integer exerciseId) {
        String sql5 = "SELECT we.id, \n" + "" +
                "we.workout_id,\n" +
                "       we.exercise_id,\n" +
                "       we.sets,\n" +
                "       we.reps,\n" +
                "       we.weight,\n" +
                "       we.rest_seconds,\n" +
                "       we.position,\n" +
                "       e.name,\n" +
                "       e.muscle_group,\n" +
                "       e.difficulty\n" +
                "FROM workout_exercises we\n" +
                "JOIN exercises e ON e.id = we.exercise_id\n" +
                "WHERE we.workout_id = ?\n" +
                "AND we.exercise_id = ?";

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql5);
            st.setInt(1, workoutId);
            st.setInt(2, exerciseId);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateWorkoutExercise(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public List<WorkoutExercise> findByWorkoutId(Integer workoutId) {
        String sql6 = """
                    SELECT we.id,
                        we.workout_id,
                           we.exercise_id,
                           we.sets,
                           we.reps,
                           we.weight,
                           we.rest_seconds,
                           we.position,
                           e.name,
                           e.muscle_group,
                           e.difficulty
                    FROM workout_exercises we
                    JOIN exercises e ON e.id = we.exercise_id
                    WHERE we.workout_id = ?
                    ORDER BY we.position
                """;

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql6);
            st.setInt(1, workoutId);
            rs = st.executeQuery();
            List<WorkoutExercise> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(instantiateWorkoutExercise(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public List<WorkoutExercise> findAll() {
        String sql7 = "SELECT we.id, we.workout_id,\n" +
                "       we.exercise_id,\n" +
                "       we.sets,\n" +
                "       we.reps,\n" +
                "       we.weight,\n" +
                "       we.rest_seconds,\n" +
                "       we.position,\n" +
                "       e.name,\n" +
                "       e.muscle_group,\n" +
                "       e.difficulty\n" +
                "FROM workout_exercises we\n" +
                "JOIN exercises e ON e.id = we.exercise_id\n" +
                "ORDER BY we.workout_id, we.position";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql7);
            rs = st.executeQuery();

            List<WorkoutExercise> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(instantiateWorkoutExercise(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void batchInsert(List<WorkoutExercise> list) {

        String sql8 = "INSERT INTO workout_exercises (workout_id, exercise_id, sets, reps, weight, rest_seconds, position) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql8);
            for (WorkoutExercise workoutExercise : list) {
                st.setInt(1, workoutExercise.getWorkoutId());
                st.setInt(2, workoutExercise.getExercise().getId());
                st.setInt(3, workoutExercise.getSets());
                st.setInt(4, workoutExercise.getReps());
                st.setDouble(5, workoutExercise.getWeight());
                st.setInt(6, workoutExercise.getRestSeconds());
                st.setInt(7, workoutExercise.getPosition());
                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }

    }

    private WorkoutExercise instantiateWorkoutExercise(ResultSet rs) throws SQLException {

        WorkoutExercise we = new WorkoutExercise();

        we.setId(rs.getInt("id"));
        we.setWorkoutId(rs.getInt("workout_id"));

        Exercise ex = new Exercise();
        ex.setId(rs.getInt("exercise_id"));
        ex.setName(rs.getString("name"));
        ex.setMuscleGroup(rs.getString("muscle_group"));
        ex.setDifficulty(Difficulty.valueOf(rs.getString("difficulty")));

        we.setExercise(ex);

        we.setSets(rs.getInt("sets"));
        we.setReps(rs.getInt("reps"));
        we.setWeight(rs.getDouble("weight"));
        we.setRestSeconds(rs.getInt("rest_seconds"));
        we.setPosition(rs.getInt("position"));

        return we;
    }
}
