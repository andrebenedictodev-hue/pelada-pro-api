package com.assovio.peladapro.peladaproapi.domain.repository;

import com.assovio.peladapro.peladaproapi.domain.model.RankingEntry;

import java.util.List;
import java.util.UUID;

public interface RankingRepository {
    RankingEntry upsertGlobal(RankingEntry entry);
    RankingEntry upsertByEvent(UUID eventId, RankingEntry entry);
    List<RankingEntry> getGlobalRanking();
    List<RankingEntry> getEventRanking(UUID eventId);
    void clearEvent(UUID eventId);
}
