package com.assovio.peladapro.peladaproapi.domain.model;

import java.util.UUID;

public class RankingEntry {

    private UUID userId;
    private String nickname;
    private int goals;
    private int assists;
    private int cards;
    private int points;

    public RankingEntry() {
    }

    public RankingEntry(UUID userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getCards() {
        return cards;
    }

    public void setCards(int cards) {
        this.cards = cards;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
