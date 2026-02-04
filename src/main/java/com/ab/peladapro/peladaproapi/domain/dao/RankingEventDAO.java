package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.RankingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingEventDAO extends JpaRepository<RankingEvent, Long> {
    Optional<RankingEvent> findFirstByEventIdAndUserId(String eventId, String userId);
    List<RankingEvent> findByEventId(String eventId);
    void deleteByEventId(String eventId);
}
