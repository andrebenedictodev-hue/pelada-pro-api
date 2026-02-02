package com.assovio.peladapro.peladaproapi.infra.repository;

import com.assovio.peladapro.peladaproapi.domain.model.MatchEvent;
import com.assovio.peladapro.peladaproapi.domain.repository.MatchEventRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryMatchEventRepository implements MatchEventRepository {

    private final ConcurrentHashMap<UUID, List<MatchEvent>> store = new ConcurrentHashMap<>();

    @Override
    public MatchEvent save(MatchEvent matchEvent) {
        store.computeIfAbsent(matchEvent.getEventId(), key -> new ArrayList<>()).add(matchEvent);
        return matchEvent;
    }

    @Override
    public List<MatchEvent> findByEventId(UUID eventId) {
        return new ArrayList<>(store.getOrDefault(eventId, List.of()));
    }

    @Override
    public Optional<MatchEvent> findLastByEventId(UUID eventId) {
        List<MatchEvent> list = store.get(eventId);
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(list.size() - 1));
    }

    @Override
    public Optional<MatchEvent> deleteLastByEventId(UUID eventId) {
        List<MatchEvent> list = store.get(eventId);
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.remove(list.size() - 1));
    }
}
