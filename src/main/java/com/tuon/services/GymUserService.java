package com.tuon.services;

import com.tuon.db.DAO.GymUserDAO;
import com.tuon.db.DAOImpl.GymUserDAOImpl;
import com.tuon.db.connection.DbConnection;
import com.tuon.entities.GymUser;
import com.tuon.exceptions.ServiceException;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;

public class GymUserService {


    public GymUser createUser(GymUser user) {
        try {
            Connection conn = DbConnection.getConnection();
            GymUserDAO userDAO = new GymUserDAOImpl(conn);
            validateForCreate(user);
            userDAO.insert(user);
            return user;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    private void validateForCreate(GymUser user) {
        if (user == null) {
            throw new ServiceException("User cannot be null");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new ServiceException("User name cannot be null or empty");
        }
        if (user.getAge() <= 0) {
            throw new ServiceException("User age must be greater than 0");
        }
        if (user.getHeight() <= 0) {
            throw new ServiceException("User height must be greater than 0");
        }
        if (user.getWeight() <= 0) {
            throw new ServiceException("User weight must be greater than 0");
        }
    }

    public GymUser updateUser(GymUser user) {
        try {
            Connection conn = DbConnection.getConnection();
            GymUserDAO userDAO = new GymUserDAOImpl(conn);
            validateForUpdate(user);
            userDAO.update(user);
            return user;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    private void validateForUpdate(GymUser user) {
        validateForCreate(user);
        if (user == null) {
            throw new ServiceException("User cannot be null");
        }
        if (user.getId() == null) {
            throw new ServiceException("User id cannot be null");
        }
    }

    public void deleteUser(Integer id) {
        try {
            Connection conn = DbConnection.getConnection();
            GymUserDAO userDAO = new GymUserDAOImpl(conn);
            if (id == null || id <= 0) {
                throw new ServiceException("User id must be greater than 0");
            }
            userDAO.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public GymUser findById(Integer id) {
        try {
            Connection conn = DbConnection.getConnection();
            GymUserDAO userDAO = new GymUserDAOImpl(conn);
            if (id == null || id <= 0) {
                throw new ServiceException("User id must be greater than 0");
            }
            return userDAO.findById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<GymUser> findAll() {
        try {
            Connection conn = DbConnection.getConnection();
            GymUserDAO userDAO = new GymUserDAOImpl(conn);
            return userDAO.findAll();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();
        }
    }

    public List<GymUser> findInvalidUser(){
        try{
            Connection conn = DbConnection.getConnection();
            GymUserDAO userDAO = new GymUserDAOImpl(conn);
            return userDAO.findAll().stream().filter(user -> user.getAge() <= 0 || user.getAge() > 100 || user.getHeight() <= 0 || user.getWeight() <= 0).toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            DbConnection.closeConnection();

        }
    }

    public List<GymUser> classifyFitnessLevel(GymUser user){
        try{
            Connection conn = DbConnection.getConnection();
            GymUserDAO userDAO = new GymUserDAOImpl(conn);
            return userDAO.findAll().stream().sorted(Comparator.comparingDouble(GymUser::calculateBMI).reversed()).toList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }finally {
            DbConnection.closeConnection();
        }
    }
}