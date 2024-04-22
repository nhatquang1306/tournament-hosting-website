package com.techelevator.controller;

import com.techelevator.dao.MatchDao;
import com.techelevator.model.Match;
import com.techelevator.model.Tournament;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping
public class MatchController {

    private MatchDao dao;
    public MatchController(MatchDao dao) {
        this.dao = dao;
    }

    // get all matches for a tournament
    @RequestMapping(path = "tournaments/{id}/matches", method = RequestMethod.GET)
    public List<Match> getAllMatches(@PathVariable int id) {
        List<Match> matches = dao.getAllMatches(id);
        if (matches.size() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No bracket has been generated for this tournament.");
        } else {
            return matches;
        }
    }

    // update result of a match
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "tournaments/{id}/match", method = RequestMethod.PUT)
    public List<Match> updateMatch(@RequestBody Match match, @PathVariable int id) {
        return dao.updateMatch(match);
    }

    // update result of final match
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @RequestMapping(path = "tournaments/{id}/final", method = RequestMethod.PUT)
    public Match finalMatch(@RequestBody Match match, @PathVariable int id) {
        return dao.finalMatch(match);
    }
}
