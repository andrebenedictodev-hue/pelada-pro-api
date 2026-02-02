package com.assovio.peladapro.peladaproapi.api.model.output;

import com.assovio.peladapro.peladaproapi.domain.model.LiveStatus;

import java.util.List;
import java.util.UUID;

public class LiveStateOutput {

    private long startedAtServerEpochMs;
    private long accumulatedTimeMs;
    private LiveStatus status;
    private int scoreA;
    private int scoreB;
    private List<UUID> currentTeamA;
    private List<UUID> currentTeamB;
    private List<UUID> queue;

    public long getStartedAtServerEpochMs() {
        return startedAtServerEpochMs;
    }

    public void setStartedAtServerEpochMs(long startedAtServerEpochMs) {
        this.startedAtServerEpochMs = startedAtServerEpochMs;
    }

    public long getAccumulatedTimeMs() {
        return accumulatedTimeMs;
    }

    public void setAccumulatedTimeMs(long accumulatedTimeMs) {
        this.accumulatedTimeMs = accumulatedTimeMs;
    }

    public LiveStatus getStatus() {
        return status;
    }

    public void setStatus(LiveStatus status) {
        this.status = status;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    public List<UUID> getCurrentTeamA() {
        return currentTeamA;
    }

    public void setCurrentTeamA(List<UUID> currentTeamA) {
        this.currentTeamA = currentTeamA;
    }

    public List<UUID> getCurrentTeamB() {
        return currentTeamB;
    }

    public void setCurrentTeamB(List<UUID> currentTeamB) {
        this.currentTeamB = currentTeamB;
    }

    public List<UUID> getQueue() {
        return queue;
    }

    public void setQueue(List<UUID> queue) {
        this.queue = queue;
    }
}
