package com.assovio.peladapro.peladaproapi.infra.repository;

import com.assovio.peladapro.peladaproapi.domain.model.Participante;
import com.assovio.peladapro.peladaproapi.domain.repository.ParticipanteRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryParticipanteRepository implements ParticipanteRepository {

    private final ConcurrentHashMap<UUID, Participante> store = new ConcurrentHashMap<>();

    @Override
    public Participante save(Participante participante) {
        store.put(participante.getId(), participante);
        return participante;
    }

    @Override
    public Optional<Participante> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Participante> findByEventIdAndUserId(UUID eventId, UUID userId) {
        return store.values().stream()
                .filter(p -> eventId.equals(p.getEventId()) && userId.equals(p.getUserId()))
                .findFirst();
    }

    @Override
    public List<Participante> findByEventId(UUID eventId) {
        return store.values().stream()
                .filter(p -> eventId.equals(p.getEventId()))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }

    @Override
    public long countByEventId(UUID eventId) {
        return store.values().stream().filter(p -> eventId.equals(p.getEventId())).count();
    }
}
