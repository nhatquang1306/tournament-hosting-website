package com.techelevator.dao;

import com.techelevator.model.TeamRequest;


import java.sql.SQLException;
import java.util.List;

public interface TeamRequestDao {

    public void createRequest(int senderId, String receiverUsername, String createdBy) throws SQLException;
    public void acceptRequestAndUpdateTeam(int requestId, int userId);
    public int getUserIdByUsername(String username);
    public void declineRequest(int requestId);
    void declineAll(int userId);
    public List<TeamRequest> getRequestsForUser(int userId);
    public void sendJoinRequest(int senderId, int receiverId, int teamId) throws SQLException;

    public int getTeamCaptainId(int teamId);


    List<TeamRequest> getJoinRequestsForCaptain(int userId);
    public boolean isTeamCaptain(String username, int teamId);
    public void acceptRequestToJoin(int requestId);
    public void declineRequestToJoin(int requestId);
}
