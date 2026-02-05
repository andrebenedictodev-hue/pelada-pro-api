package com.ab.peladapro.peladaproapi.domain.model;

import com.ab.peladapro.peladaproapi.domain.model.contracts.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "ranking_evento")
@SQLRestriction("deleted_at IS NULL")
public class RankingEvent extends EntityBase {

    @Column(name = "evento_uuid", nullable = false)
    private String eventId;

    @Column(name = "usuario_uuid", nullable = false)
    private String userId;

    @Column(name = "apelido", nullable = false)
    private String nickname;

    @Column(name = "gols", nullable = false)
    private int goals;

    @Column(name = "assistencias", nullable = false)
    private int assists;

    @Column(name = "cartoes", nullable = false)
    private int cards;

    @Column(name = "pontos", nullable = false)
    private int points;

    @Column(name = "partidas", nullable = false)
    private int matches;

    @Column(name = "vitorias", nullable = false)
    private int wins;

    @Column(name = "derrotas", nullable = false)
    private int losses;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }
}
