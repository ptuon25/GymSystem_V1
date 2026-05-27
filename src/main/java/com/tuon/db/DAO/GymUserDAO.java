package com.tuon.db.DAO;

import com.tuon.entities.GymUser;

import java.util.List;

public interface GymUserDAO {

    void insert(GymUser user);

    void update(GymUser user);

    void deleteById(Integer id);

    GymUser findById(Integer id);

    List<GymUser> findAll();

}
