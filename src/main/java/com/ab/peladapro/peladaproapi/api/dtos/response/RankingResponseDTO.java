package com.ab.peladapro.peladaproapi.api.dtos.response;

import java.util.List;

public class RankingResponseDTO {

    private List<RankingEntryResponseDTO> entries;

    public List<RankingEntryResponseDTO> getEntries() {
        return entries;
    }

    public void setEntries(List<RankingEntryResponseDTO> entries) {
        this.entries = entries;
    }
}
