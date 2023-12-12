package com.techelevator.controller;

import com.techelevator.dao.TournamentDao;
import com.techelevator.model.Match;
import com.techelevator.model.Tournament;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tournaments")
public class TournamentController {
    private TournamentDao dao;
    public TournamentController(TournamentDao dao) {
        this.dao = dao;
    }
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public Tournament createTournament(@RequestBody Tournament tournament) {
        return dao.createTournament(tournament);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<Tournament> getAllTournament() {
        return dao.getAllTournaments();
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Tournament getTournamentById(@PathVariable int id) {
        return dao.getTournament(id);
    }

    @PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/team/{id}")
    public Tournament getTournamentByTeamId(@PathVariable int id) {
        return dao.getByTeamId(id);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/{id}/bracket", method = RequestMethod.POST)
    public void generateBracket(@PathVariable int id) {
        dao.generateBracket(id);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/{id}/update", method = RequestMethod.PUT)
    public Tournament updateTournament(@RequestBody Tournament tournament, @PathVariable int id) {
        return dao.updateTournament(tournament);
    }
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/{id}/reset", method = RequestMethod.PUT)
    public void resetTournament(@PathVariable int id) {
        dao.resetTournament(id);
    }
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/{id}/start", method = RequestMethod.PUT)
    public void startTournament(@PathVariable int id) {
        dao.startTournament(id);
    }
    @RequestMapping(path = "/game/{game}", method = RequestMethod.GET)
    public void getByGame(@PathVariable String game) {
        dao.getByGame(game);
    }
    @RequestMapping(path = "/status/{status}", method = RequestMethod.GET)
    public void getByStatus(@PathVariable String status) {
        dao.getByStatus(status);
    }
    @RequestMapping(path = "/featured/{number}", method = RequestMethod.GET)
    public List<Tournament> getFeatured(@PathVariable int number) {
        return dao.getFeatured(number);
    }
}
