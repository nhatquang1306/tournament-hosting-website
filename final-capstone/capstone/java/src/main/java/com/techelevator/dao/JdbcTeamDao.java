package com.techelevator.dao;

import com.techelevator.model.Team;
import com.techelevator.model.Tournament;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTeamDao implements TeamDAO{

    private JdbcTemplate template;

    public JdbcTeamDao(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    // 1. this is to be used to list all teams in the database
    @Override
    public List<Team> getAllTeams(int tournamentId) {
        List<Team> teamList = new ArrayList<>();
        String sqlGetAllTeams = "SELECT * " +
                "FROM tournament AS tn " +
                "JOIN tournament_team AS tt ON tn.tournament_id = tt.tournament_id " +
                "JOIN team AS t ON tt.team_id = t.team_id " +
                "WHERE tn.tournament_id = ?";
        SqlRowSet result = template.queryForRowSet(sqlGetAllTeams, tournamentId);

        while (result.next()) {
            int teamId = result.getInt("team_id");
            String teamName = result.getString("team_name");
            String createdBy = result.getString("created_by");
            boolean openToPublic = result.getBoolean("open_to_public");
            Team team = new Team(teamId, teamName, createdBy, openToPublic);
            teamList.add(team);
        }
        return teamList;
    }

    // 2. select a team by Id
    @Override
    public Team getTeamById(int teamId) {
        String sql = "SELECT * FROM team WHERE team_id = ?";
        SqlRowSet result = template.queryForRowSet(sql, teamId);
        Team team = null;
        if (result.next()) {
            int id = result.getInt("team_id");
            String name = result.getString("team_name");
            String createdBy = result.getString("created_by");
            boolean openToPublic = result.getBoolean("open_to_public");
            team = new Team(id, name, createdBy, openToPublic);
        }
        return team;
    }

    // 3. this is similar to the next method, but this can be used on any team
    // regardless of  which tournament they are in
    @Override
    public List<Team> getTeamByName(String searchTerm) {
        List<Team> teamList = new ArrayList<>();
        String sql = "SELECT * FROM team WHERE team_name ILIKE ?";

        SqlRowSet result = template.queryForRowSet(sql, "%" + searchTerm + "%");
        while (result.next()) {
            int teamId = result.getInt("team_id");
            String teamName = result.getString("team_name");
            String createdBy = result.getString("created_by");
            boolean openToPublic = result.getBoolean("open_to_public");
            Team team = new Team(teamId, teamName, createdBy, openToPublic);
            teamList.add(team);
        }
        return teamList;
    }

    // 4. this is used to search through the teams within a specific tournament
    @Override
    public List<Team> filterTeams(int tournament_id, String searchTerm) {
        List<Team> teamList = new ArrayList<>();
        String sqlGetTeamsBySearchTerm = "SELECT * " +
                "FROM tournament AS tn " +
                "JOIN tournament_team AS tt ON tn.tournament_id = tt.tournament_id " +
                "JOIN team AS t ON tt.team_id = t.team_id " +
                "WHERE tn.tournament_id = ? " +
                "AND team_name ILIKE ?";
        SqlRowSet result = template.queryForRowSet(sqlGetTeamsBySearchTerm, tournament_id, "%" + searchTerm + "%");
        while (result.next()) {
            int teamId = result.getInt("team_id");
            String teamName = result.getString("team_name");
            String createdBy = result.getString("created_by");
            boolean openToPublic = result.getBoolean("open_to_public");
            Team team = new Team(teamId, teamName, createdBy, openToPublic);
            teamList.add(team);
        }
        return teamList;
    }

    // 5. When this method is used, the team is created, The logged in user is automatically assigned to the team
    // the "createdBy" column for team automatically is set to match the username of who created that team
    @Override
    public void addTeam(int tournamentId, Team newTeam, String createdBy, boolean openToPublic) {
        int teamId = 0;
        String teamName = newTeam.getTeamName();
        System.out.println(teamId + " " + teamName);

        String sql = "INSERT INTO team (team_name, created_by, open_to_public) VALUES (?, ?, ?) RETURNING team_id";

        try {
            teamId = template.queryForObject(sql, Integer.class, teamName, createdBy, openToPublic);
        } catch (Exception e) {
            if (e.getMessage().contains("value too long for type character varying"))
            throw new Error("Team name cannot exceed 15 characters.");
        }
        String sql2 = "INSERT INTO tournament_team (tournament_id, team_id) VALUES (?, ?); " +
                "UPDATE users SET team_id = ? WHERE username = ?; ";
        template.update(sql2, tournamentId, teamId, teamId, createdBy);



        String sql3 = "DELETE FROM team_request WHERE status = 'join_request_pending' AND sender_id = (SELECT user_id FROM users WHERE username = ?);";
        template.update(sql3, createdBy);

        // Check if there are 8 teams in the current tournament
        int teamCount = template.queryForObject("SELECT COUNT(*) FROM tournament_team WHERE tournament_id = ?", Integer.class, tournamentId);
        if (teamCount == 8) {
            // Update tournament status to "upcoming"
            template.update("UPDATE tournament SET status = 'Upcoming' WHERE tournament_id = ?", tournamentId);
        }
    }

    public void changeTournamentStatusToUpcoming () {

    }


    // 6. deletes team by ID
    @Override
    public void removeTeam(String status, int tournamentId, int teamId) {

        String sqlForRemove = "DELETE FROM tournament_team WHERE team_id = ?;" +
                "DELETE FROM team_request WHERE team_id = ?;";
        template.update(sqlForRemove, teamId, teamId);
        if (status.equals("Upcoming")) {
            String sql = "UPDATE tournament SET status = 'Open' WHERE tournament_id = ?";
            template.update(sql, tournamentId);
        }


    }

    // 7. can edit the team name
    @Override
    public void editTeam(Team teamToEdit) {
        // can target by name instead of ID
        String sql = "UPDATE team " + "SET team_name = ? WHERE team_id = ?";
        int teamsUpdated = template.update(sql, teamToEdit.getTeamName(), teamToEdit.getTeamId());
        if (teamsUpdated == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // 8. Removes a team member and also deletes their invites/requests associated with that team
    @Override
    public void removeTeamMember(int userId, int teamId, String teamCaptainUsername) {

        if (isTeamCaptain(teamId, teamCaptainUsername)) {
            String updateUsersSql = "UPDATE users SET team_id = NULL WHERE user_id = ?";
            template.update(updateUsersSql, userId);

            String deleteRequestSql = "DELETE FROM team_request WHERE team_id = ? AND receiver_id = ?";
            template.update(deleteRequestSql, teamId, userId);
        } else {
            throw new Error("Only the team captain can remove team members.");
        }
    }

    // Service method that is used in the method above
    private boolean isTeamCaptain(int teamId, String teamCaptainUsername) {
        String getTeamCaptainSql = "SELECT created_by FROM team WHERE team_id = ?";
        String teamCaptain = template.queryForObject(getTeamCaptainSql, String.class, teamId);
        return teamCaptain.equals(teamCaptainUsername);
    }

    // 9. Used to filter through all teams that are open to public
    @Override
    public List<Team> filterOpenTeams(int tournament_id, String searchTerm) {
        List<Team> teamList = new ArrayList<>();
        String sqlGetTeamsBySearchTerm = "SELECT * " +
                "FROM tournament AS tn " +
                "JOIN tournament_team AS tt ON tn.tournament_id = tt.tournament_id " +
                "JOIN team AS t ON tt.team_id = t.team_id " +
                "WHERE tn.tournament_id = ? " +
                "AND team_name ILIKE ? AND open_to_public = true";
        SqlRowSet result = template.queryForRowSet(sqlGetTeamsBySearchTerm, tournament_id, "%" + searchTerm + "%");
        while (result.next()) {
            int teamId = result.getInt("team_id");
            String teamName = result.getString("team_name");
            String createdBy = result.getString("created_by");
            boolean openToPublic = result.getBoolean("open_to_public");
            Team team = new Team(teamId, teamName, createdBy, openToPublic);
            teamList.add(team);
        }
        return teamList;
    }

    @Override
    public void assignCaptain(int teamId, String username) {
        String sql = "UPDATE team SET created_by = ? WHERE team_id = ?;" +
                "DELETE FROM team_request WHERE team_id = ? AND status = 'Pending'";
        template.update(sql, username, teamId, teamId);
    }

}
