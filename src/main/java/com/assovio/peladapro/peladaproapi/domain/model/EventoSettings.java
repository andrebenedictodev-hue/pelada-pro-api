package com.assovio.peladapro.peladaproapi.domain.model;

public class EventoSettings {

    private int teamSize;
    private int timerDurationSec;
    private int goalsLimit;
    private MatchMode mode;

    public EventoSettings() {
    }

    public EventoSettings(int teamSize, int timerDurationSec, int goalsLimit, MatchMode mode) {
        this.teamSize = teamSize;
        this.timerDurationSec = timerDurationSec;
        this.goalsLimit = goalsLimit;
        this.mode = mode;
    }

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
