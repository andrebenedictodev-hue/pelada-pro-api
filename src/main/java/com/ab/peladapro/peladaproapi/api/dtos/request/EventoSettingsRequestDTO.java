package com.ab.peladapro.peladaproapi.api.dtos.request;

import com.ab.peladapro.peladaproapi.domain.model.MatchMode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EventoSettingsRequestDTO {

    @Min(1)
    private int teamSize;

    @Min(60)
    private int timerDurationSec;

    @Min(1)
    private int goalsLimit;

    @NotNull
    private MatchMode mode;

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public int getTimerDurationSec() {
        return timerDurationSec;
    }

    public void setTimerDurationSec(int timerDurationSec) {
        this.timerDurationSec = timerDurationSec;
    }

    public int getGoalsLimit() {
        return goalsLimit;
    }

    public void setGoalsLimit(int goalsLimit) {
        this.goalsLimit = goalsLimit;
    }

    public MatchMode getMode() {
        return mode;
    }

    public void setMode(MatchMode mode) {
        this.mode = mode;
    }
}
