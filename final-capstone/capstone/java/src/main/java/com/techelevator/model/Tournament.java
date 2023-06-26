package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tournament {
    @JsonProperty("tournament_id")
    private int tournamentId;
    @JsonProperty("tournament_name")
    private String name;
    @JsonProperty("location")
    private String location;
    @JsonProperty("date_and_time")
    private Timestamp dateTime;
    @JsonProperty("game")
    private String game;
    @JsonProperty("entry_fee")
    private String entryFee;
    @JsonProperty("prize")
    private String prize;
    @JsonProperty("tournament_owner")
    private String owner;
    @JsonProperty("format")
    private String format;
    @JsonProperty("rules")
    private String rules;
    @JsonProperty("description")
    private String description;
    @JsonProperty("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public static List<Match> generateBracket(List<Integer> teamIdList, int id, Timestamp dateTime, String format) {
        Collections.shuffle(teamIdList);
        List<Match> matchList = new ArrayList<>();
        for (int i = 0; i < teamIdList.size(); i += 2) {
            matchList.add(new Match(id, teamIdList.get(i), teamIdList.get(i + 1), format, 1, dateTime));
        }
        int numMatches = teamIdList.size() / 4;
        int numRounds = (int)(Math.log(teamIdList.size()) / Math.log(2)) + 1;
        for (int i = 2; i <= numRounds; i++) {
            matchList.addAll(Collections.nCopies(numMatches, new Match(id, format, i, dateTime)));
            if (i != numRounds - 1) numMatches /= 2;
        }
        numMatches = teamIdList.size() / 4;
        for (int i = -1; i >= numRounds * -1; i--) {
            matchList.addAll(Collections.nCopies(numMatches, new Match(id, format, i, dateTime)));
            if (i % 2 == 0) numMatches /= 2;
        }
        return matchList;
    }
}
