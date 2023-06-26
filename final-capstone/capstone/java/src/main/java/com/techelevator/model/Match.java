package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Match {
    @JsonProperty("match_id")
    private int matchId;
    @JsonProperty("tournament_id")
    private int tournamentId;
    @JsonProperty("first_team_id")
    private int firstTeamId;
    @JsonProperty("second_team_id")
    private int secondTeamId;
    @JsonProperty("winner_id")
    private int winnerId;
    @JsonProperty("first_score")
    private int firstScore;
    @JsonProperty("second_score")
    private int secondScore;
    @JsonProperty("match_format")
    private String matchFormat;
    @JsonProperty("round")
    private int round;
    @JsonProperty("date_and_time")
    private Timestamp dateTime;

    public Match(int tournamentId, int firstTeamId, int secondTeamId, String matchFormat, int round, Timestamp dateTime) {
        this.tournamentId = tournamentId;

        this.firstTeamId = firstTeamId;
        this.secondTeamId = secondTeamId;
        this.matchFormat = matchFormat;
        this.dateTime = dateTime;
        this.round = round;
        firstScore = 0;
        secondScore = 0;
    }

    public Match(int tournamentId, String matchFormat, int round, Timestamp dateTime) {
        this.tournamentId = tournamentId;
        this.matchFormat = matchFormat;
        this.round = round;
        this.dateTime = dateTime;
        firstScore = 0;
        secondScore = 0;
    }

    public Match() {
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public int getFirstScore() {
        return firstScore;
    }

    public void setFirstScore(int firstScore) {
        this.firstScore = firstScore;
    }

    public int getSecondScore() {
        return secondScore;
    }

    public void setSecondScore(int secondScore) {
        this.secondScore = secondScore;
    }

    public String getMatchFormat() {
        return matchFormat;
    }

    public void setMatchFormat(String matchFormat) {
        this.matchFormat = matchFormat;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public void setFirstTeamId(int firstTeamId) {
        this.firstTeamId = firstTeamId;
    }

    public void setSecondTeamId(int secondTeamId) {
        this.secondTeamId = secondTeamId;
    }

    public int getFirstTeamId() {
        return firstTeamId;
    }

    public int getSecondTeamId() {
        return secondTeamId;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public int getRound() {
        return round;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

}
