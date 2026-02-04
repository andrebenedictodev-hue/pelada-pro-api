package com.ab.peladapro.peladaproapi.api.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class LiveTeamsRequestDTO {

    @NotNull
    private List<List<UUID>> teams;
    @NotNull
    private List<UUID> queue;

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
}
