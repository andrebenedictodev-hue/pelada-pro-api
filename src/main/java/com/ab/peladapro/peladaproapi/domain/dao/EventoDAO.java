package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventoDAO extends JpaRepository<Evento, Long> {
    Optional<Evento> findFirstByUuid(String uuid);
    List<Evento> findByOwnerId(String ownerId);
}
