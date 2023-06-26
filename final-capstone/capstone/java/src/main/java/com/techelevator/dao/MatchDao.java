package com.techelevator.dao;

import com.techelevator.model.Match;

import java.util.List;

public interface MatchDao {
    Match getMatch(int matchId);
    List<Match> getAllMatches(int tournamentId);
    List<Match> updateMatch(Match match);
    Match finalMatch(Match match);
}
