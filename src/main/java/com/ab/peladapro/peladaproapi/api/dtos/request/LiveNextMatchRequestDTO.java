package com.ab.peladapro.peladaproapi.api.dtos.request;

import java.util.List;
import java.util.UUID;

public class LiveNextMatchRequestDTO {

    private List<UUID> outgoingPlayerIds;

    public List<UUID> getOutgoingPlayerIds() {
        return outgoingPlayerIds;
    }

    public void setOutgoingPlayerIds(List<UUID> outgoingPlayerIds) {
        this.outgoingPlayerIds = outgoingPlayerIds;
    }
}
