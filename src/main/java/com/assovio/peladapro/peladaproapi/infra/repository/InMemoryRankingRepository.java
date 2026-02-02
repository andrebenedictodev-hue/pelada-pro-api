package com.assovio.peladapro.peladaproapi.infra.repository;

import com.assovio.peladapro.peladaproapi.domain.model.RankingEntry;
import com.assovio.peladapro.peladaproapi.domain.repository.RankingRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRankingRepository implements RankingRepository {

    private final ConcurrentHashMap<UUID, RankingEntry> global = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, RankingEntry>> perEvent = new ConcurrentHashMap<>();

    @Override
    public RankingEntry upsertGlobal(RankingEntry entry) {
        global.put(entry.getUserId(), entry);
        return entry;
    }

    @Override
    public RankingEntry upsertByEvent(UUID eventId, RankingEntry entry) {
        perEvent.computeIfAbsent(eventId, key -> new ConcurrentHashMap<>()).put(entry.getUserId(), entry);
        return entry;
    }

    @Override
    public List<RankingEntry> getGlobalRanking() {
        return sortByPoints(global.values());
    }

    @Override
    public List<RankingEntry> getEventRanking(UUID eventId) {
        Map<UUID, RankingEntry> map = perEvent.get(eventId);
        if (map == null) {
            return List.of();
        }
        return sortByPoints(map.values());
    }

    @Override
    public void clearEvent(UUID eventId) {
        perEvent.remove(eventId);
    }

    private List<RankingEntry> sortByPoints(Iterable<RankingEntry> entries) {
        List<RankingEntry> list = new ArrayList<>();
        entries.forEach(list::add);
        list.sort(Comparator.comparingInt(RankingEntry::getPoints).reversed()
                .thenComparingInt(RankingEntry::getGoals).reversed());
        return list;
    }
}
