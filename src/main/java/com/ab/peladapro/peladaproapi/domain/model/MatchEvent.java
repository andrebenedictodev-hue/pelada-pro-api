package com.ab.peladapro.peladaproapi.domain.model;

import com.ab.peladapro.peladaproapi.domain.model.contracts.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "evento_partida")
@SQLRestriction("deleted_at IS NULL")
public class MatchEvent extends EntityBase {

    @Column(name = "evento_uuid", nullable = false)
    private String eventId;

    @Column(name = "jogador_uuid", nullable = false)
    private String playerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private MatchEventType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "time", nullable = false)
    private MatchTeam team;

    @Column(name = "tempo_partida_ms", nullable = false)
    private long matchTimeMs;

    public MatchEvent() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public MatchEventType getType() {
        return type;
    }

    public void setType(MatchEventType type) {
        this.type = type;
    }

    public MatchTeam getTeam() {
        return team;
    }

    public void setTeam(MatchTeam team) {
        this.team = team;
    }

    public long getMatchTimeMs() {
        return matchTimeMs;
    }

    public void setMatchTimeMs(long matchTimeMs) {
        this.matchTimeMs = matchTimeMs;
    }
}
