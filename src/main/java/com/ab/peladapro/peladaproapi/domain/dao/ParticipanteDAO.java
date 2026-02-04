package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipanteDAO extends JpaRepository<Participante, Long> {
    Optional<Participante> findFirstByUuid(String uuid);
    Optional<Participante> findFirstByEventIdAndUserId(String eventId, String userId);
    List<Participante> findByEventId(String eventId);
    long countByEventId(String eventId);
    void deleteByEventId(String eventId);
}
