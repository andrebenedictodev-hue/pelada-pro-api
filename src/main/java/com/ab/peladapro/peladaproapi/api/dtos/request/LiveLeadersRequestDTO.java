package com.ab.peladapro.peladaproapi.api.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public class LiveLeadersRequestDTO {

    @NotNull
    @Size(min = 2, max = 2)
    private List<UUID> leaders;

    public List<UUID> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<UUID> leaders) {
        this.leaders = leaders;
    }
}
