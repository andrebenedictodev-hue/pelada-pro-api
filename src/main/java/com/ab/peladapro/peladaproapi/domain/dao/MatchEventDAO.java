package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchEventDAO extends JpaRepository<MatchEvent, Long> {
    List<MatchEvent> findByEventId(String eventId);
    Optional<MatchEvent> findFirstByEventIdOrderByCreatedAtDesc(String eventId);
    void deleteByEventId(String eventId);
}
