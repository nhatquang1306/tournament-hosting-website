package com.techelevator.dao;

import com.techelevator.model.TeamRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Component
public class JdbcTeamRequestDao implements TeamRequestDao{

    private JdbcTemplate template;

    public JdbcTeamRequestDao(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    // 1. Creates an invite to send to other registered users.
    // will only work if the sender is also the captain(createdBy) of the team
    @Override
    public void createRequest(int senderId, String receiverUsername, String createdBy) throws SQLException {

        String teamCreatorSql = "SELECT team_id FROM team WHERE created_by = ? AND team_id IN " +
                "(SELECT team_id FROM users WHERE user_id = ?)";
        int teamId = template.queryForObject(teamCreatorSql, Integer.class, createdBy, senderId);

        if (teamId > 0) {
            String receiverIdSql = "SELECT user_id FROM users WHERE username = ?";
            int receiverId;
            try {
                receiverId = template.queryForObject(receiverIdSql, Integer.class, receiverUsername);
            } catch (Exception e) {
                throw new SQLException("This user doesn't exist.");
            }
            String sql = "SELECT * FROM team_request WHERE sender_id = ? AND receiver_id = ? AND status = 'Pending'";
            if (template.queryForRowSet(sql, senderId, receiverId).next()) {
                throw new SQLException("You have already sent an invite to this user.");
            }


            String sql2 = "INSERT INTO team_request (sender_id, receiver_id, team_id) VALUES (?, ?, ?)";
            template.update(sql2, senderId, receiverId, teamId);
        } else {
            throw new IllegalArgumentException("Only the team captain can send invites.");
        }
    }

    // 2. this method is used when a request is accepted.
    // it changes the user's team to match the team_id that is associated with the request
    // then in the same method it changes status from 'pending' to 'accepted'
    @Override
    public void acceptRequestAndUpdateTeam(int requestId, int userId) {
        String getTeamIdSql = "SELECT team_id FROM team_request WHERE team_request_id = ?";
        int teamId = template.queryForObject(getTeamIdSql, Integer.class, requestId);

        String updateTeamSql = "UPDATE users SET team_id = ? WHERE user_id = ?";
        template.update(updateTeamSql, teamId, userId);

        String sql = "DELETE FROM team_request WHERE (status = 'join_request_pending' AND sender_id = ?);";
        template.update(sql, userId);

        String updateRequestSql = "UPDATE team_request SET status = 'accepted' WHERE team_request_id = ?";
        template.update(updateRequestSql, requestId);
    }

    // 3. this is a service method that is called in the controller method: acceptRequestAndUpdateTeam
    // we can use principal.getName to get the username, then use this method to get the Id that's associated to that username
    public int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        return template.queryForObject(sql, Integer.class, username);
    }

    // 4. changes request status to 'declined'
    @Override
    public void declineRequest(int requestId) {
        String updateRequestSql = "UPDATE team_request SET status = 'declined' WHERE team_request_id = ?";
        template.update(updateRequestSql, requestId);
    }

    @Override
    public void declineAll(int userId) {
        String sql = "UPDATE team_request SET status = 'declined' WHERE receiver_id = ? AND status = 'Pending'";
        template.update(sql, userId);
    }


    // 5. get request so the user that is logged in can see all of the requests sent to them(Pending)
    @Override
    public List<TeamRequest> getRequestsForUser(int userId) {
        String sql = "SELECT * FROM team_request WHERE receiver_id = ? AND status = 'Pending'";
        return template.query(sql, (rs, rowNum) -> {

            int teamRequestId = rs.getInt("team_request_id");
            int senderId = rs.getInt("sender_id");
            int receiverId = rs.getInt("receiver_id");
            int teamId = rs.getInt("team_id");
            String status = rs.getString("status");
            return new TeamRequest(teamRequestId, senderId, receiverId, teamId, status);
        }, userId);
    }

    // 6. Join Requests: this method is for anonymous users trying to join a team that is open to public
    // senderId is the one requesting to join, receiverId is the team captain
    // the "status" is set to 'join_request_pending'.
    // Use method 7 to filter
    @Override
    public void sendJoinRequest(int senderId, int receiverId, int teamId) throws SQLException {
        String sql1 = "SELECT * FROM team_request WHERE sender_id = ? AND receiver_id = ? AND status = 'join_request_pending'";
        if (template.queryForRowSet(sql1, senderId, receiverId).next()) {
            throw new SQLException("You have already requested to join this team.");
        }
        String sql2 = "SELECT * FROM team_request WHERE sender_id = ? AND receiver_id = ? AND status = 'Pending'";
        if (template.queryForRowSet(sql2, receiverId, senderId).next()) {
            throw new SQLException("You have an invite to join this team. Please accept it at the home page.");
        }
        String sql = "INSERT INTO team_request (sender_id, receiver_id, team_id, status) VALUES (?, ?, ?, 'join_request_pending')";
        template.update(sql, senderId, receiverId, teamId);
    }

    // This is a service method that is used in sendJoinRequest(controller).
    @Override
    public int getTeamCaptainId(int teamId) {
        String sql = "SELECT created_by FROM team WHERE team_id = ? ";
        String createdBy = template.queryForObject(sql, String.class, teamId);
        String sql2 = "SELECT user_id FROM users WHERE username = ?";
        return template.queryForObject(sql2, Integer.class, createdBy);
    }

    // 7. This method gets all of the requests to join
    @Override
    public List<TeamRequest> getJoinRequestsForCaptain(int userId) {
        String sql = "SELECT * FROM team_request WHERE receiver_id = ? AND status = 'join_request_pending'";
        return template.query(sql, (rs, rowNum) -> {
            int teamRequestId = rs.getInt("team_request_id");
            int senderId = rs.getInt("sender_id");
            int receiverId = rs.getInt("receiver_id");
            int teamId = rs.getInt("team_id");
            String status = rs.getString("status");
            return new TeamRequest(teamRequestId, senderId, receiverId, teamId, status);
        }, userId);
    }

    // Service method used to check if the user is the team captain
    @Override
    public boolean isTeamCaptain(String username, int teamId) {
        int teamCaptainId = getTeamCaptainId(teamId);
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ? AND username = ?";
        int count = template.queryForObject(sql, Integer.class, teamCaptainId, username);
        return count > 0;
    }

    // 8. Accepts requests to join, adds that member to the team, and updates the request status
    @Override
    public void acceptRequestToJoin(int requestId) {
        String getSenderIdSql = "SELECT sender_id FROM team_request WHERE team_request_id = ?";
        int senderId = template.queryForObject(getSenderIdSql, Integer.class, requestId);

        String getTeamIdSql = "SELECT team_id FROM team_request WHERE team_request_id = ?";
        int teamId = template.queryForObject(getTeamIdSql, Integer.class, requestId);

        String updateTeamSql = "UPDATE users SET team_id = ? WHERE user_id = ?";
        template.update(updateTeamSql, teamId, senderId);

        String sql = "DELETE FROM team_request WHERE (status = 'join_request_pending' AND sender_id = ?);";
        template.update(sql, senderId);

        String updateRequestSql = "UPDATE team_request SET status = 'accepted' WHERE team_request_id = ?";
        template.update(updateRequestSql, requestId);
    }

    // 9. Decline requests to join
    @Override
    public void declineRequestToJoin(int requestId) {
        String updateRequestSql = "UPDATE team_request SET status = 'declined' WHERE team_request_id = ?";
        template.update(updateRequestSql, requestId);
    }
}

