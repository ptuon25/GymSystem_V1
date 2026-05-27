package com.tuon.db.DAOImpl;

import com.tuon.db.DAO.WorkoutDAO;
import com.tuon.exceptions.DbException;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.Exercise;
import com.tuon.entities.Workout;
import com.tuon.entities.WorkoutExercise;
import com.tuon.enums.Difficulty;
import com.tuon.enums.WorkoutStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDAOImpl implements WorkoutDAO {

    private final Connection conn;
    private PreparedStatement st;

    public WorkoutDAOImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(Workout workout) {
        String sql1 = "INSERT INTO workouts (user_id, name, status, date) VALUES (?, ?, ?, ?)";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
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
        String sql2 = "UPDATE workouts SET name = ?, status = ?, date = ? WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql2)) {
            st.setString(1, workout.getName());
            st.setString(2, workout.getStatus().toString());
            st.setObject(3, workout.getDate(), Types.TIMESTAMP);
            st.setInt(4, workout.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql3 = "DELETE FROM workouts WHERE id = ?";
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
    public Workout findById(Integer id) {
        String sql4 = "SELECT id, user_id, name, status, date FROM workouts WHERE id = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql4);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateWorkout(rs);
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
        String sql5 = "SELECT id, user_id, name, status, date FROM workouts ORDER BY date DESC";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql5);
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
}