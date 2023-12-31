package com.techelevator.dao;

import com.techelevator.model.Team;
import com.techelevator.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User getUserById(int userId);

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password, String role);

    List<User> findByTeamId(int id);

    void leaveTeam(String username, Team team, String status);
}
