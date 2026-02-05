package com.ab.peladapro.peladaproapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class EventoSettings {

    @Column(name = "tamanho_time", nullable = false)
    private int teamSize;
    @Column(name = "duracao_timer_sec", nullable = false)
    private int timerDurationSec;
    @Column(name = "limite_gols", nullable = false)
    private int goalsLimit;
    @Enumerated(EnumType.STRING)
    @Column(name = "modo", nullable = false)
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
