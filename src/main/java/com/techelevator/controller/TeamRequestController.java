package com.techelevator.controller;

import com.techelevator.dao.TeamRequestDao;
import com.techelevator.model.TeamRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin
public class TeamRequestController {

    @Autowired
    private TeamRequestDao dao;


    // send invite for a user to join team as team leader
    @RequestMapping(path = "/requests", method = RequestMethod.POST)
    public void createRequest(@RequestParam int senderId, @RequestParam String receiverUsername, Principal principal) {
        String createdBy = principal.getName();
        try {
            dao.createRequest(senderId, receiverUsername, createdBy);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // accept an invite to join team
    @RequestMapping(path = "/requests/{requestId}/accept", method = RequestMethod.POST)
    public void acceptRequest(@PathVariable int requestId, Principal principal) {
        String username = principal.getName(); // Get the username from the Principal
        int userId = dao.getUserIdByUsername(username); // Get the Id associated to that username

        dao.acceptRequestAndUpdateTeam(requestId, userId);
    }

    // decline an invite to join team
    @RequestMapping(path = "/requests/{requestId}/decline", method = RequestMethod.POST)
    public void declineRequest(@PathVariable int requestId) {
        dao.declineRequest(requestId);
    }

    // decline all requests to join team as team leader
    @RequestMapping(path = "/requests/{userId}/decline-all", method = RequestMethod.PUT)
    public void declineAll(@PathVariable int userId) {
        dao.declineAll(userId);
    }

    // see all invites to join teams
    @RequestMapping(path = "/requests", method = RequestMethod.GET)
    public List<TeamRequest> getRequestsForUser(Principal principal) {
        String username = principal.getName();
        int userId = dao.getUserIdByUsername(username);
        return serviceGetRequestsForUser(userId);
    }

    // service method that is used in the method above
    public List<TeamRequest> serviceGetRequestsForUser(int userId) {
        return dao.getRequestsForUser(userId);
    }

    // send join request to a team leader to join their team
    @RequestMapping(path = "/teams/{teamId}/join-request", method = RequestMethod.POST)
    public void sendJoinRequest(@PathVariable int teamId, Principal principal) {
        // Get the sender ID from the Principal
        String username = principal.getName(); // Get the username from the Principal
        int senderId = dao.getUserIdByUsername(username); // Get the Id associated to that username

        // Get the receiver ID (team captain) based on the team ID
        int receiverId = dao.getTeamCaptainId(teamId); // Service method for this in the DAO

        // Send the join request
        try {
            dao.sendJoinRequest(senderId, receiverId, teamId);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    // get all join requests as team leader
    @RequestMapping(path = "/requests/join-requests", method = RequestMethod.GET)
    public List<TeamRequest> getJoinRequestsForCaptain(Principal principal) throws Exception{
        String username = principal.getName();
        int userId = dao.getUserIdByUsername(username);
        return dao.getJoinRequestsForCaptain(userId);
    }

    // accept a request to join team as team leader
    @RequestMapping(path = "/join-requests/{requestId}/accept", method = RequestMethod.PUT)
    public void acceptRequestToJoin(@PathVariable int requestId) {
        dao.acceptRequestToJoin(requestId);
    }

    // decline a request to join team as team leader
    @RequestMapping(path = "/join-requests/{requestId}/decline", method = RequestMethod.PUT)
    public void declineRequestToJoin(@PathVariable int requestId) {
        dao.declineRequestToJoin(requestId);
    }
}
