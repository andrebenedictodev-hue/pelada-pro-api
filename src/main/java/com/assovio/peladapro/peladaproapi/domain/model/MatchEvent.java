package com.assovio.peladapro.peladaproapi.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MatchEvent {

    private UUID id;
    private UUID eventId;
    private UUID playerId;
    private MatchEventType type;
    private MatchTeam team;
    private long matchTimeMs;
    private OffsetDateTime createdAt;

    public MatchEvent() {
    }

    public MatchEvent(UUID id, UUID eventId, UUID playerId, MatchEventType type, MatchTeam team, long matchTimeMs,
            OffsetDateTime createdAt) {
        this.id = id;
        this.eventId = eventId;
        this.playerId = playerId;
        this.type = type;
        this.team = team;
        this.matchTimeMs = matchTimeMs;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
