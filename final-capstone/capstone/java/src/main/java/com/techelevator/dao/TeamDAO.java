package com.techelevator.dao;

import com.techelevator.model.Team;

import java.util.List;

public interface TeamDAO {

    public List<Team> getAllTeams(int tournamentId);
    public Team getTeamById(int teamId);
    public List<Team> getTeamByName(String searchTerm);
    public List<Team> filterTeams(int tournament_id, String searchTerm);
    public void addTeam(int tournamentId, Team newTeam, String createdBy, boolean openToPublic);
    public void removeTeam(String status, int tournamentId, int teamId);
    public void editTeam(Team teamToEdit);
    public void removeTeamMember(int userId, int teamId, String teamCaptainUsername);
    public List<Team> filterOpenTeams(int tournament_id, String searchTerm);
    void assignCaptain(int teamId, String username);
}
