package com.tuon.db.DAOImpl;

import com.tuon.db.DAO.GymUserDAO;
import com.tuon.exceptions.DbException;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.GymUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymUserDAOImpl implements GymUserDAO {


    private final Connection conn;


    public GymUserDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(GymUser user) {

        String sql1 = "INSERT INTO gym_user (name, age, height, weight) VALUES (?, ?, ?, ?)";

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, user.getName());
            st.setInt(2, user.getAge());
            st.setDouble(3, user.getHeight());
            st.setDouble(4, user.getWeight());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    user.setId(id);
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
    public void update(GymUser user) {

        String sql2 = "UPDATE gym_user SET name = ?, age = ?, height = ?, weight = ? WHERE id = ?";

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql2);
            st.setString(1, user.getName());
            st.setInt(2, user.getAge());
            st.setDouble(3, user.getHeight());
            st.setDouble(4, user.getWeight());
            st.setInt(5, user.getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("No user found with the provided ID.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {

        String sql3 = "DELETE FROM gym_user WHERE id = ?";

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql3);
            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("No user found with the provided ID.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeStatement(st);
        }
    }

    @Override
    public GymUser findById(Integer id) {

        String sql4 = "SELECT * FROM gym_user WHERE id = ?";

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(sql4);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateGymUser(rs);
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
    public List<GymUser> findAll() {

        String sql5 = "SELECT * FROM gym_user ORDER BY name";

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(sql5);
            rs = st.executeQuery();
            List<GymUser> list = new ArrayList<>();

            while (rs.next()) {
                list.add(instantiateGymUser(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DbConnection.closeResultSet(rs);
            DbConnection.closeStatement(st);
        }
    }

    private GymUser instantiateGymUser(ResultSet rs) throws SQLException {
        GymUser user = new GymUser();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setAge(rs.getInt("age"));
        user.setHeight(rs.getDouble("height"));
        user.setWeight(rs.getDouble("weight"));
        return user;
    }
}
