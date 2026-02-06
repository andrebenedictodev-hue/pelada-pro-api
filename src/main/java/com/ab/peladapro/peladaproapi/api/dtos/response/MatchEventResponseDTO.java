package com.ab.peladapro.peladaproapi.api.dtos.response;

import com.ab.peladapro.peladaproapi.domain.model.MatchEventType;
import com.ab.peladapro.peladaproapi.domain.model.MatchTeam;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MatchEventResponseDTO {

    private UUID id;
    private UUID eventId;
    private UUID playerId;
    private UUID assistPlayerId;
    private MatchEventType type;
    private MatchTeam team;
    private long matchTimeMs;
    private OffsetDateTime createdAt;

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

    public UUID getAssistPlayerId() {
        return assistPlayerId;
    }

    public void setAssistPlayerId(UUID assistPlayerId) {
        this.assistPlayerId = assistPlayerId;
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
