package com.assovio.peladapro.peladaproapi.api.model.input;

import com.assovio.peladapro.peladaproapi.domain.model.MatchEventType;
import com.assovio.peladapro.peladaproapi.domain.model.MatchTeam;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class MatchEventInput {

    @NotNull
    private UUID playerId;

    @NotNull
    private MatchEventType type;

    @NotNull
    private MatchTeam team;

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
