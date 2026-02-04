package com.ab.peladapro.peladaproapi.api.dtos.response;

import com.ab.peladapro.peladaproapi.domain.model.LiveStatus;

import java.util.List;
import java.util.UUID;

public class LiveStateResponseDTO {

    private long startedAtServerEpochMs;
    private long accumulatedTimeMs;
    private LiveStatus status;
    private int scoreA;
    private int scoreB;
    private List<List<UUID>> teams;
    private List<UUID> queue;
    private List<UUID> leaders;

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

    public List<List<UUID>> getTeams() {
        return teams;
    }

    public void setTeams(List<List<UUID>> teams) {
        this.teams = teams;
    }

    public List<UUID> getQueue() {
        return queue;
    }

    public void setQueue(List<UUID> queue) {
        this.queue = queue;
    }

    public List<UUID> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<UUID> leaders) {
        this.leaders = leaders;
    }
}
