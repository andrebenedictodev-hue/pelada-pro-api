package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.LiveState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LiveStateDAO extends JpaRepository<LiveState, Long> {
    Optional<LiveState> findFirstByEventId(String eventId);
    void deleteByEventId(String eventId);
}
