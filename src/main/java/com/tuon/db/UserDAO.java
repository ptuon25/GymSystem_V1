package com.tuon.db;

import com.tuon.entities.GymUser;

import java.util.List;

public interface UserDAO {

    void insert(GymUser user);

    void update(GymUser user);

    void deleteById(Integer id);

    GymUser findById(Integer id);

    List<GymUser> findAll();

}
