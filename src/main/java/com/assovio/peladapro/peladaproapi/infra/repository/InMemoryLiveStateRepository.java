package com.assovio.peladapro.peladaproapi.infra.repository;

import com.assovio.peladapro.peladaproapi.domain.model.LiveState;
import com.assovio.peladapro.peladaproapi.domain.repository.LiveStateRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryLiveStateRepository implements LiveStateRepository {

    private final ConcurrentHashMap<UUID, LiveState> store = new ConcurrentHashMap<>();

    @Override
    public LiveState save(UUID eventId, LiveState state) {
        store.put(eventId, state);
        return state;
    }

    @Override
    public Optional<LiveState> findByEventId(UUID eventId) {
        return Optional.ofNullable(store.get(eventId));
    }

    @Override
    public int count() {
        return store.size();
    }
}
