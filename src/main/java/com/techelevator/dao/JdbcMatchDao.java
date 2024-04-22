package com.techelevator.dao;

import com.techelevator.model.Bracket;
import com.techelevator.model.Match;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcMatchDao implements MatchDao {

    private final JdbcTemplate jdbcTemplate;
    public JdbcMatchDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // get a match by match id
    @Override
    public Match getMatch(int matchId) {
        Match match = new Match();
        String sql = "SELECT * FROM matches WHERE match_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, matchId);
        if (results.next()) {
            match = mapRowToMatch(results);
        }
        return match;
    }

    // get all matches for a tournament
    @Override
    public List<Match> getAllMatches(int tournamentId) {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT * FROM matches WHERE tournament_id = ? ORDER BY match_id";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, tournamentId);
        while (results.next()) {
            matches.add(mapRowToMatch(results));
        }
        return matches;
    }

    // update match info
    @Override
    public List<Match> updateMatch(Match match) {
        // set the score and winner for the match
        String sql1 = "UPDATE matches SET first_score = ?, second_score = ?, winner_id = ? WHERE match_id = ?";
        jdbcTemplate.update(sql1, match.getFirstScore(), match.getSecondScore(), match.getWinnerId() == 0 ? null : match.getWinnerId(), match.getMatchId());

        // if there's no winner, return
        int winningPoint = (Integer.parseInt(match.getMatchFormat().replaceAll("^[^\\d]+", "")) + 1)/ 2;
        if (match.getFirstScore() != winningPoint && match.getSecondScore() != winningPoint) return null;

        // if there's a winner, calculate who goes on to next match by using the bracket class
        List<Match> allMatches = getAllMatches(match.getTournamentId());
        Bracket bracket = new Bracket(allMatches);
        String sql2 = "UPDATE matches SET first_team_id = ?, second_team_id = ? WHERE match_id = ?";
        List<Match> matches = bracket.progress(match);
        matches.forEach(nextMatch -> {
            jdbcTemplate.update(sql2, nextMatch.getFirstTeamId() == 0 ? null : nextMatch.getFirstTeamId(),
                    nextMatch.getSecondTeamId() == 0 ? null : nextMatch.getSecondTeamId(), nextMatch.getMatchId());
        });
        return matches;

    }

    // update final match info
    @Override
    public Match finalMatch(Match match) {
        String sql1 = "UPDATE matches SET winner_id = ?, first_score = ?, second_score = ? WHERE match_id = ?";
        jdbcTemplate.update(sql1, match.getWinnerId() == 0 ? null : match.getWinnerId(), match.getFirstScore(), match.getSecondScore(), match.getMatchId());

        // if the winner is from the winner bracket, set tournament status to finished
        if (match.getWinnerId() == match.getFirstTeamId() || (match.getWinnerId() != 0 && match.getRound() == 0)) {
            String sql2 = "UPDATE tournament SET status = ? WHERE tournament_id = ?";
            jdbcTemplate.update(sql2, "Finished", match.getTournamentId());
        // if the winner is from the loser bracket, add another match to the tournament for the final round
        } else if (match.getWinnerId() != 0) {
            int loserId = match.getWinnerId() == match.getFirstTeamId() ? match.getSecondTeamId() : match.getFirstTeamId();
            String sql3 = "INSERT INTO matches(tournament_id, first_team_id, second_team_id, match_format, round, date_and_time) VALUES " +
                    "(?, ?, ?, ?, ?, ?) RETURNING match_id;";
            int id = jdbcTemplate.queryForObject(sql3, int.class, match.getTournamentId(), match.getWinnerId(), loserId,
                    match.getMatchFormat(), 0, match.getDateTime());
            return getMatch(id);
        }
        return null;
    }




    private Match mapRowToMatch(SqlRowSet rs) {
        Match match = new Match();
        match.setMatchId(rs.getInt("match_id"));
        match.setTournamentId(rs.getInt("tournament_id"));
        match.setFirstTeamId(rs.getInt("first_team_id"));
        match.setSecondTeamId(rs.getInt("second_team_id"));
        match.setWinnerId(rs.getInt("winner_id"));
        match.setMatchFormat(rs.getString("match_format"));
        match.setFirstScore(rs.getInt("first_score"));
        match.setSecondScore(rs.getInt("second_score"));
        match.setRound(rs.getInt("round"));
        match.setDateTime(rs.getTimestamp("date_and_time"));
        return match;
    }
}
