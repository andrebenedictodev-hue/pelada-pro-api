package com.assovio.peladapro.peladaproapi.domain.repository;

import com.assovio.peladapro.peladaproapi.domain.model.Participante;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipanteRepository {
    Participante save(Participante participante);
    Optional<Participante> findById(UUID id);
    Optional<Participante> findByEventIdAndUserId(UUID eventId, UUID userId);
    List<Participante> findByEventId(UUID eventId);
    void deleteById(UUID id);
    long countByEventId(UUID eventId);
}
