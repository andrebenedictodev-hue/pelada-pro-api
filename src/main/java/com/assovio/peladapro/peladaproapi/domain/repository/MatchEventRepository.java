package com.assovio.peladapro.peladaproapi.domain.repository;

import com.assovio.peladapro.peladaproapi.domain.model.MatchEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchEventRepository {
    MatchEvent save(MatchEvent matchEvent);
    List<MatchEvent> findByEventId(UUID eventId);
    Optional<MatchEvent> findLastByEventId(UUID eventId);
    Optional<MatchEvent> deleteLastByEventId(UUID eventId);
}
