package com.techelevator.dao;

import com.techelevator.model.Match;
import com.techelevator.model.Tournament;

import java.util.List;

public interface TournamentDao {
    Tournament createTournament(Tournament tournament);
    Tournament getTournament(int id);
    Tournament getByTeamId(int id);
    void generateBracket(int id);
    List<Tournament> getAllTournaments();
    Tournament updateTournament(Tournament tournament);
    void startTournament(int id);
    List<Tournament> getByGame(String game);
    List<Tournament> getByStatus(String status);
    List<Tournament> getFeatured(int number);
    void resetTournament(int id);
}
