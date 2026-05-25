package com.tuon.db;

import com.tuon.entities.GymUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {


    private Connection conn;

    public UserDAOImpl(){

    }

    public UserDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(GymUser user) {

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("INSERT INTO gym_user (name, age, height, weight) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE gym_user SET name = ?, age = ?, height = ?, weight = ? WHERE id = ?");
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

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM gym_user WHERE id = ?");
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
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM gym_user WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateGymUser(rs);
            } else {
                return null; // No user found with the provided ID
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

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT * FROM gym_user ORDER BY name");
            rs = st.executeQuery();

            List<GymUser> list = new ArrayList<>();

            while(rs.next()){
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
