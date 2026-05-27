package com.tuon.db.DAOImpl;

import com.tuon.db.DAO.ExerciseDAO;
import com.tuon.exceptions.DbException;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Exercise;
import com.tuon.enums.Difficulty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ExerciseDAOImpl implements ExerciseDAO {

    private final Connection conn;

    public ExerciseDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Exercise exercise) {
        String sql1 = "INSERT INTO exercises (name, muscle_group, difficulty) VALUES (?, ?, ?)";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, exercise.getName());
            st.setString(2, exercise.getMuscleGroup());
            st.setString(3, exercise.getDifficulty().name());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    exercise.setId(id);
                } else {
                    throw new DbException("Unexpected error! Error retrieving generated ID.");
                }
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void update(Exercise exercise) {

        String sql2 = "UPDATE exercises SET name = ?, muscle_group = ?, difficulty = ? WHERE id = ?";

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql2);
            ;
            st.setString(1, exercise.getName());
            st.setString(2, exercise.getMuscleGroup());
            st.setString(3, exercise.getDifficulty().name());
            st.setInt(4, exercise.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {

        String sql3 = "DELETE FROM exercises WHERE id = ?";

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql3);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public Exercise findById(Integer id) {

        String sql4 = "SELECT id, name, muscle_group, difficulty FROM exercises WHERE id = ?";

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql4);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateExercise(rs);
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
    public List<Exercise> findAll() {

        String sql5 = "SELECT id, name, muscle_group, difficulty FROM exercises ORDER BY name";
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(sql5);
            rs = st.executeQuery();
            List<Exercise> list = new java.util.ArrayList<>();
            while (rs.next()) {
                list.add(instantiateExercise(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    public List<Exercise> findExercisesByWorkoutId(Integer workoutId) {
        String sql6 = "SELECT e.id, e.name, e.muscle_group, e.difficulty FROM exercises e " +
                "INNER JOIN workout_exercises we ON e.id = we.exercise_id " +
                "WHERE we.workout_id = ? " +
                "ORDER BY we.position";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql6);
            st.setInt(1, workoutId);
            rs = st.executeQuery();
            List<Exercise> list = new ArrayList<>();
            while (rs.next()) {
                list.add(instantiateExercise(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }


    private Exercise instantiateExercise(ResultSet rs) throws SQLException {
        Exercise exercise = new Exercise();
        exercise.setId(rs.getInt("id"));
        exercise.setName(rs.getString("name"));
        exercise.setMuscleGroup(rs.getString("muscle_group"));
        exercise.setDifficulty(Difficulty.valueOf(rs.getString("difficulty")));

        return exercise;
    }
}
