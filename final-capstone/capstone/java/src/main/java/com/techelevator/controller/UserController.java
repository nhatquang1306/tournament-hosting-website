package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.model.Team;
import com.techelevator.model.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping
public class UserController {

    private UserDao dao;
    public UserController(UserDao dao) {
        this.dao = dao;
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public User getByUserId(@PathVariable int id) {
        return dao.getUserById(id);
    }
    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
    public List<User> getUsersByTeamId(@PathVariable int id) {
        return dao.findByTeamId(id);
    }
    @RequestMapping(path = "/user/leave/{status}", method = RequestMethod.PUT)
    public void leaveTeam(Principal principal, @RequestBody Team team, @PathVariable String status) {
        dao.leaveTeam(principal.getName(), team, status);
    }
}
