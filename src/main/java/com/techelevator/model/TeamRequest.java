package com.techelevator.model;

public class TeamRequest {

    private int teamRequestId;
    private int senderId;
    private int receiverId;
    private int teamId;
    private String status;

    public TeamRequest (int teamRequestId, int senderId, int receiverId, int teamId, String status) {
        this.teamRequestId = teamRequestId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.teamId = teamId;
        this.status = status;
    }

    public int getTeamRequestId() {
        return teamRequestId;
    }

    public void setTeamRequestId(int teamRequestId) {
        this.teamRequestId = teamRequestId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
