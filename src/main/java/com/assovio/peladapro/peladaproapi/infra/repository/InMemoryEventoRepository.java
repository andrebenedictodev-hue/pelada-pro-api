package com.assovio.peladapro.peladaproapi.infra.repository;

import com.assovio.peladapro.peladaproapi.domain.model.Evento;
import com.assovio.peladapro.peladaproapi.domain.repository.EventoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryEventoRepository implements EventoRepository {

    private final ConcurrentHashMap<UUID, Evento> store = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, UUID> inviteIndex = new ConcurrentHashMap<>();

    @Override
    public Evento save(Evento evento) {
        store.put(evento.getId(), evento);
        if (evento.getInviteCode() != null) {
            inviteIndex.put(evento.getInviteCode(), evento.getId());
        }
        return evento;
    }

    @Override
    public Optional<Evento> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Evento> findByInviteCode(String inviteCode) {
        UUID eventId = inviteIndex.get(inviteCode);
        if (eventId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(store.get(eventId));
    }

    @Override
    public List<Evento> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Evento> findByOwnerId(UUID ownerId) {
        return store.values().stream()
                .filter(evento -> ownerId.equals(evento.getOwnerId()))
                .toList();
    }
}
