package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findFirstByUuid(String uuid);
    Optional<Usuario> findFirstByEmail(String email);
    Optional<Usuario> findFirstByNickname(String nickname);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
