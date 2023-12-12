package com.techelevator.model;

public class Team {
    private int teamId;
    private String teamName;
    private String createdBy;
    private boolean openToPublic;

    // this page will mainly be accessible once
    // on the /tournaments page. you can create teams and invite other users for specific tournaments

    public Team (int teamId, String teamName, String createdBy, boolean openToPublic) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.createdBy = createdBy;
        this.openToPublic = openToPublic;
    }

    public int getTeamId () {
        return teamId;
    }

    public void setTeamId (int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName () {
        return teamName;
    }

    public void setTeamName (String teamName) {
        this.teamName = teamName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isOpenToPublic() {
        return openToPublic;
    }

    public void setOpenToPublic(boolean openToPublic) {
        this.openToPublic = openToPublic;
    }
}
