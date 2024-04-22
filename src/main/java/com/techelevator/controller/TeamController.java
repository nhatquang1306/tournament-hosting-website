package com.techelevator.controller;

import com.techelevator.dao.TeamDAO;
import com.techelevator.model.Team;
import com.techelevator.model.TeamRequest;
import com.techelevator.model.Tournament;
import com.techelevator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
public class TeamController {

    @Autowired
    private TeamDAO dao;

    // get all teams for a tournament
    @RequestMapping(path="/tournaments/{tournamentId}/allTeams", method = RequestMethod.GET)
    public List<Team> getAllTeams(@PathVariable int tournamentId) {
        List<Team> allTeams = dao.getAllTeams(tournamentId);
        return allTeams;
    }

    // filter teams by search term for a tournament
    @RequestMapping(path = "/tournaments/{tournamentId}/filter", method = RequestMethod.GET)
    public List<Team> filterTeams(@PathVariable int tournamentId, @RequestParam String searchTerm) {
        List<Team> filteredList = dao.filterTeams(tournamentId, searchTerm);
        return filteredList;
    }

    // get team by team id
    @RequestMapping(path = "/teams/{teamId}", method = RequestMethod.GET)
    public Team selected (@PathVariable int teamId) {
        Team selectedTeam = dao.getTeamById(teamId);
        return selectedTeam;
    }

    // filter teams by search term
    @RequestMapping(path = "/teams/filter", method = RequestMethod.GET)
    public List<Team> selected (@RequestParam String searchTerm) {
        List<Team> filteredList = dao.getTeamByName(searchTerm);
        return filteredList;
    }

    // add team to a tournament
    @RequestMapping(path = "/tournaments/{tournamentId}/addTeam", method = RequestMethod.POST)
    public void addTeam(@PathVariable int tournamentId, @RequestBody Team team, @RequestParam boolean openToPublic, Principal principal) {
        String createdBy = principal.getName();
        team.setCreatedBy(createdBy);
        team.setOpenToPublic(openToPublic);
        try {
            dao.addTeam(tournamentId, team, createdBy, openToPublic);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    // edit team info as leader
    @RequestMapping(path = "/teams/editTeam", method = RequestMethod.PUT)
    public void editTeam(@RequestBody Team team) {
        dao.editTeam(team);
    }

    // remove team member as leader
    @RequestMapping(path = "/teams/{teamId}/members/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeTeamMember(@PathVariable int teamId, @PathVariable int userId, Principal principal) {
        String teamCaptainUsername = principal.getName(); // Get the username of the logged-in user
        try {
            dao.removeTeamMember(userId, teamId, teamCaptainUsername);
            return ResponseEntity.ok("Team member removed successfully.");
        } catch (HttpClientErrorException.Unauthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only the team captain can remove team members.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove team member.");
        }
    }

    // filter open teams within a tournament
    @RequestMapping(path = "/tournaments/{tournamentId}/filterOpen", method = RequestMethod.GET)
    public List<Team> filterOpenTeams(@PathVariable int tournamentId, @RequestParam String searchTerm) {
        List<Team> filteredList = dao.filterOpenTeams(tournamentId, searchTerm);
        return filteredList;
    }

    // remove a team as organizer
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "teams/{teamId}/remove", method = RequestMethod.PUT)
    public void remove(@RequestBody Tournament tournament, @PathVariable int teamId) {
        dao.removeTeam(tournament.getStatus(), tournament.getTournamentId(), teamId);
    }

    // assign new captain as team leader
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "teams/{teamId}/assign", method = RequestMethod.PUT)
    public void assignCaptain(@PathVariable int teamId, @RequestParam String username) {
        dao.assignCaptain(teamId, username);
    }
}
























