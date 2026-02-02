package com.assovio.peladapro.peladaproapi.api.model.output;

import java.util.List;

public class RankingOutput {

    private List<RankingEntryOutput> entries;

    public List<RankingEntryOutput> getEntries() {
        return entries;
    }

    public void setEntries(List<RankingEntryOutput> entries) {
        this.entries = entries;
    }
}
