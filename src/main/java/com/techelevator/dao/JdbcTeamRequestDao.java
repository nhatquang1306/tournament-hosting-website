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

    // invite an user to join team as team leader
    @Override
    public void createRequest(int senderId, String receiverUsername, String createdBy) throws SQLException {
        // get the team id
        String teamCreatorSql = "SELECT team_id FROM team WHERE created_by = ? AND team_id IN " +
                "(SELECT team_id FROM users WHERE user_id = ?)";
        int teamId = template.queryForObject(teamCreatorSql, Integer.class, createdBy, senderId);

        if (teamId > 0) {
            String receiverIdSql = "SELECT user_id FROM users WHERE username = ?";
            int receiverId;
            // throw error if the invitee doesn't exist
            try {
                receiverId = template.queryForObject(receiverIdSql, Integer.class, receiverUsername);
            } catch (Exception e) {
                throw new SQLException("This user doesn't exist.");
            }

            // throw error if an invite has already been sent to the same user
            String sql = "SELECT * FROM team_request WHERE sender_id = ? AND receiver_id = ? AND status = 'Pending'";
            if (template.queryForRowSet(sql, senderId, receiverId).next()) {
                throw new SQLException("You have already sent an invite to this user.");
            }

            // send invite to the user
            String sql2 = "INSERT INTO team_request (sender_id, receiver_id, team_id) VALUES (?, ?, ?)";
            template.update(sql2, senderId, receiverId, teamId);
        } else {
            throw new IllegalArgumentException("Only the team captain can send invites.");
        }
    }

    // accept an invite to join team
    @Override
    public void acceptRequestAndUpdateTeam(int requestId, int userId) {
        String getTeamIdSql = "SELECT team_id FROM team_request WHERE team_request_id = ?";
        int teamId = template.queryForObject(getTeamIdSql, Integer.class, requestId);

        String updateTeamSql = "UPDATE users SET team_id = ? WHERE user_id = ?";
        template.update(updateTeamSql, teamId, userId);

        // delete all join requests of the user
        String sql = "DELETE FROM team_request WHERE (status = 'join_request_pending' AND sender_id = ?);";
        template.update(sql, userId);

        // set status of the invite to accepted
        String updateRequestSql = "UPDATE team_request SET status = 'accepted' WHERE team_request_id = ?";
        template.update(updateRequestSql, requestId);
    }

    // get user id by username
    public int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        return template.queryForObject(sql, Integer.class, username);
    }

    // decline an invite to join team
    @Override
    public void declineRequest(int requestId) {
        String updateRequestSql = "UPDATE team_request SET status = 'declined' WHERE team_request_id = ?";
        template.update(updateRequestSql, requestId);
    }

    // decline all invites to join teams
    @Override
    public void declineAll(int userId) {
        String sql = "UPDATE team_request SET status = 'declined' WHERE receiver_id = ? AND status = 'Pending'";
        template.update(sql, userId);
    }


    // get all team invites for a user
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

    // allow a user to send a request to join a team
    @Override
    public void sendJoinRequest(int senderId, int receiverId, int teamId) throws SQLException {
        // if the user already requested to join this team, don't allow them to do it again
        String sql1 = "SELECT * FROM team_request WHERE sender_id = ? AND receiver_id = ? AND status = 'join_request_pending'";
        if (template.queryForRowSet(sql1, senderId, receiverId).next()) {
            throw new SQLException("You have already requested to join this team.");
        }

        // if the user already has an invite to join the team, cancel the request
        String sql2 = "SELECT * FROM team_request WHERE sender_id = ? AND receiver_id = ? AND status = 'Pending'";
        if (template.queryForRowSet(sql2, receiverId, senderId).next()) {
            throw new SQLException("You have an invite to join this team. Please accept it at the home page.");
        }

        // else send the join request
        String sql = "INSERT INTO team_request (sender_id, receiver_id, team_id, status) VALUES (?, ?, ?, 'join_request_pending')";
        template.update(sql, senderId, receiverId, teamId);
    }

    // get the user id of the leader of a team
    @Override
    public int getTeamCaptainId(int teamId) {
        String sql = "SELECT created_by FROM team WHERE team_id = ? ";
        String createdBy = template.queryForObject(sql, String.class, teamId);
        String sql2 = "SELECT user_id FROM users WHERE username = ?";
        return template.queryForObject(sql2, Integer.class, createdBy);
    }

    // get all the requests to join team as team leader
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

    // service method used to check if the user is the team captain
    @Override
    public boolean isTeamCaptain(String username, int teamId) {
        int teamCaptainId = getTeamCaptainId(teamId);
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ? AND username = ?";
        int count = template.queryForObject(sql, Integer.class, teamCaptainId, username);
        return count > 0;
    }

    // accept a request to join team
    @Override
    public void acceptRequestToJoin(int requestId) {
        String getSenderIdSql = "SELECT sender_id FROM team_request WHERE team_request_id = ?";
        int senderId = template.queryForObject(getSenderIdSql, Integer.class, requestId);

        String getTeamIdSql = "SELECT team_id FROM team_request WHERE team_request_id = ?";
        int teamId = template.queryForObject(getTeamIdSql, Integer.class, requestId);

        // add member to the team
        String updateTeamSql = "UPDATE users SET team_id = ? WHERE user_id = ?";
        template.update(updateTeamSql, teamId, senderId);

        // update the status of the join request
        String sql = "DELETE FROM team_request WHERE (status = 'join_request_pending' AND sender_id = ?);";
        template.update(sql, senderId);
        String updateRequestSql = "UPDATE team_request SET status = 'accepted' WHERE team_request_id = ?";
        template.update(updateRequestSql, requestId);
    }

    // decline a request to join team
    @Override
    public void declineRequestToJoin(int requestId) {
        String updateRequestSql = "UPDATE team_request SET status = 'declined' WHERE team_request_id = ?";
        template.update(updateRequestSql, requestId);
    }
}

