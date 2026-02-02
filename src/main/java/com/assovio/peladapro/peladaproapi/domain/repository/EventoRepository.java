package com.assovio.peladapro.peladaproapi.domain.repository;

import com.assovio.peladapro.peladaproapi.domain.model.Evento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventoRepository {
    Evento save(Evento evento);
    Optional<Evento> findById(UUID id);
    Optional<Evento> findByInviteCode(String inviteCode);
    List<Evento> findAll();
    List<Evento> findByOwnerId(UUID ownerId);
}
