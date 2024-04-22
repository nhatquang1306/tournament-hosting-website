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

    // create a tournament as organizer
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public Tournament createTournament(@RequestBody Tournament tournament) {
        return dao.createTournament(tournament);
    }

    // get all tournaments in the database
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<Tournament> getAllTournament() {
        return dao.getAllTournaments();
    }

    // get tournament by tournament id
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Tournament getTournamentById(@PathVariable int id) {
        return dao.getTournament(id);
    }

    // get tournament by team id
    @PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/team/{id}")
    public Tournament getTournamentByTeamId(@PathVariable int id) {
        return dao.getByTeamId(id);
    }

    // generate bracket for a tournament as organizer
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/{id}/bracket", method = RequestMethod.POST)
    public void generateBracket(@PathVariable int id) {
        dao.generateBracket(id);
    }

    // update tournament info as organizer
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/{id}/update", method = RequestMethod.PUT)
    public Tournament updateTournament(@RequestBody Tournament tournament, @PathVariable int id) {
        return dao.updateTournament(tournament);
    }

    // reset tournament and delete bracket as organizer
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/{id}/reset", method = RequestMethod.PUT)
    public void resetTournament(@PathVariable int id) {
        dao.resetTournament(id);
    }

    // start tournament as organizer
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "/{id}/start", method = RequestMethod.PUT)
    public void startTournament(@PathVariable int id) {
        dao.startTournament(id);
    }

    // filter tournaments by game type
    @RequestMapping(path = "/game/{game}", method = RequestMethod.GET)
    public void getByGame(@PathVariable String game) {
        dao.getByGame(game);
    }

    // filter tournaments by status (upcoming, active, open, finished)
    @RequestMapping(path = "/status/{status}", method = RequestMethod.GET)
    public void getByStatus(@PathVariable String status) {
        dao.getByStatus(status);
    }

    // get a specified number of featured tournaments
    @RequestMapping(path = "/featured/{number}", method = RequestMethod.GET)
    public List<Tournament> getFeatured(@PathVariable int number) {
        return dao.getFeatured(number);
    }
}
