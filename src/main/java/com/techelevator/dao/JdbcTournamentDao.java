package com.techelevator.dao;

import com.techelevator.model.Match;
import com.techelevator.model.Tournament;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTournamentDao implements TournamentDao{

    private final JdbcTemplate jdbcTemplate;
    public JdbcTournamentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // create a tournament with name, prize, time, location, game type, entry fee, format, description
    @Override
    public Tournament createTournament(Tournament tournament) {
        String sql = "INSERT INTO tournament(tournament_name, status, prize, date_and_time, location, game, entry_fee, format, rules, tournament_owner, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING tournament_id;";
        int id = jdbcTemplate.queryForObject(sql, int.class, tournament.getName(), "Open", tournament.getPrize(), tournament.getDateTime(),
                tournament.getLocation(), tournament.getGame(), tournament.getEntryFee(), tournament.getFormat(), "5v5", tournament.getOwner(), tournament.getDescription());
        return getTournament(id);
    }

    // get tournament by tournament id
    @Override
    public Tournament getTournament(int id) {
        Tournament tournament = new Tournament();
        String sql = "SELECT * FROM tournament WHERE tournament_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            tournament = mapRowToTournament(results);
        }
        return tournament;
    }

    // get tournament by team id
    @Override
    public Tournament getByTeamId(int id) {
        Tournament tournament = new Tournament();
        String sql = "SELECT * FROM tournament WHERE tournament_id = (SELECT tournament_id FROM tournament_team WHERE team_id = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            tournament = mapRowToTournament(results);
        }
        return tournament;
    }

    // generate bracket for a tournament using the bracket class
    @Override
    public void generateBracket(int id) {
        String sql1 = "SELECT team_id FROM tournament_team WHERE tournament_id = ?;";
        SqlRowSet results1 = jdbcTemplate.queryForRowSet(sql1, id);
        List<Integer> teamIds = new ArrayList<>();
        while (results1.next()) {
            teamIds.add(results1.getInt("team_id"));
        }
        Tournament tournament = getTournament(id);
        List<Match> matches = Tournament.generateBracket(teamIds, id, tournament.getDateTime(), tournament.getFormat());
        String sql2 = "INSERT INTO matches(tournament_id, first_team_id, second_team_id, match_format, round, date_and_time) VALUES " +
                "(?, ?, ?, ?, ?, ?) RETURNING match_id;";
        for (Match match : matches) {
            jdbcTemplate.queryForObject(sql2, int.class, id, match.getFirstTeamId() == 0 ? null : match.getFirstTeamId(),
                    match.getSecondTeamId() == 0 ? null : match.getSecondTeamId(), match.getMatchFormat(), match.getRound(), match.getDateTime());
        }
    }

    // get all tournaments in the database
    @Override
    public List<Tournament> getAllTournaments() {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM tournament";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            tournaments.add(mapRowToTournament(results));
        }
        return tournaments;
    }

    // update tournament info as organizer
    @Override
    public Tournament updateTournament(Tournament tournament) {
        String sql = "UPDATE tournament SET tournament_name = ?, prize = ?, date_and_time = ?, location = ?, game = ?, entry_fee = ? " +
                "WHERE tournament_id = ?";
        jdbcTemplate.update(sql, tournament.getName(), tournament.getPrize(), tournament.getDateTime(), tournament.getLocation(),
                tournament.getGame(), tournament.getEntryFee(), tournament.getTournamentId());
        return getTournament(tournament.getTournamentId());
    }

    // start tournament as organizer
    @Override
    public void startTournament(int id) {
        String statusSql = "UPDATE tournament SET status = ? WHERE tournament_id = ?";
        // delete all invites of teams in the tournaments
        String deleteSql = "UPDATE team_request SET status = 'declined' WHERE status = 'Pending' AND team_id IN (SELECT team_id FROM tournament_team WHERE tournament_id = ?)";
        jdbcTemplate.update(statusSql, "Active", id);
        jdbcTemplate.update(deleteSql, id);
    }

    // filter tournaments by game type
    @Override
    public List<Tournament> getByGame(String game) {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM tournaments WHERE game = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, game);
        while (results.next()) {
            tournaments.add(mapRowToTournament(results));
        }
        return tournaments;
    }

    // filter tournaments by status
    @Override
    public List<Tournament> getByStatus(String status) {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM tournaments WHERE status ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, status);
        while (results.next()) {
            tournaments.add(mapRowToTournament(results));
        }
        return tournaments;
    }

    // get a number of featured tournament (active tournaments will show up first)
    @Override
    public List<Tournament> getFeatured(int number) {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM tournament ORDER BY status LIMIT ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, number);
        while (results.next()) {
            tournaments.add(mapRowToTournament(results));
        }
        return tournaments;
    }

    // reset tournament and delete all matches as organizer
    @Override
    public void resetTournament(int id) {
        String sql = "DELETE FROM matches WHERE tournament_id = ?;" +
                "UPDATE tournament SET status = 'Upcoming' WHERE tournament_id = ?";
        jdbcTemplate.update(sql, id, id);
    }

    private Tournament mapRowToTournament(SqlRowSet rs) {
        Tournament tournament = new Tournament();
        tournament.setOwner(rs.getString("tournament_owner"));
        tournament.setTournamentId(rs.getInt("tournament_id"));
        tournament.setName(rs.getString("tournament_name"));
        tournament.setPrize(rs.getString("prize"));
        tournament.setStatus(rs.getString("status"));
        tournament.setDateTime(rs.getTimestamp("date_and_time"));
        tournament.setLocation(rs.getString("location"));
        tournament.setGame(rs.getString("game"));
        tournament.setEntryFee(rs.getString("entry_fee"));
        tournament.setFormat(rs.getString("format"));
        tournament.setRules(rs.getString("rules"));
        tournament.setDescription(rs.getString("description"));
        return tournament;
    }

}
