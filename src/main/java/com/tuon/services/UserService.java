package com.tuon.services;

import com.tuon.db.DbException;
import com.tuon.db.UserDAO;
import com.tuon.db.UserDAOImpl;
import com.tuon.entities.GymUser;

import java.util.List;
import java.util.Objects;

public class UserService {

    private UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = Objects.requireNonNull(userDAO, "UserDAO cannot be null");

    }

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public GymUser createUser(GymUser user){
        validateForCreate(user);
        userDAO.insert(user);
        return user;
    }

    private void validateForCreate(GymUser user) {
        if (user == null) {
            throw new DbException("User cannot be null");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new DbException("User name cannot be null or empty");
        }
        if (user.getAge() <= 0) {
            throw new DbException("User age must be greater than 0");
        }
        if (user.getHeight() <= 0) {
            throw new DbException("User height must be greater than 0");
        }
        if (user.getWeight() <= 0) {
            throw new DbException("User weight must be greater than 0");
        }
    }

    public GymUser updateUser(GymUser user){
        validateForUpdate(user);
        userDAO.update(user);
        return user;
    }

    private void validateForUpdate(GymUser user) {
        validateForCreate(user);
        if (user.getId() == null) {
            throw new DbException("User id cannot be null");
        }
    }

    public void deleteUser(Integer id){
        if (id == null || id <= 0){
            throw new DbException("User id must be greater than 0");
        }
        userDAO.deleteById(id);
    }

    public GymUser findById(Integer id){
        if (id == null || id <= 0){
            throw new DbException("User id must be greater than 0");
        }
        return userDAO.findById(id);
    }

    public List<GymUser> findAll(){
        return userDAO.findAll();
    }
}
