package com.assovio.peladapro.peladaproapi.domain.repository;

import com.assovio.peladapro.peladaproapi.domain.model.LiveState;

import java.util.Optional;
import java.util.UUID;

public interface LiveStateRepository {
    LiveState save(UUID eventId, LiveState state);
    Optional<LiveState> findByEventId(UUID eventId);
    int count();
}
