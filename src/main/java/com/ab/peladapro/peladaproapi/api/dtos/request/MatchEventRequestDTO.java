package com.ab.peladapro.peladaproapi.api.dtos.request;

import com.ab.peladapro.peladaproapi.domain.model.MatchEventType;
import com.ab.peladapro.peladaproapi.domain.model.MatchTeam;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class MatchEventRequestDTO {

    @NotNull
    private UUID playerId;

    @NotNull
    private MatchEventType type;

    @NotNull
    private MatchTeam team;

    @Min(0)
    private long matchTimeMs;

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
}
