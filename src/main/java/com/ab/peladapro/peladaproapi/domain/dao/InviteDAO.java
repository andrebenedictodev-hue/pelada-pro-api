package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteDAO extends JpaRepository<Invite, Long> {
    Optional<Invite> findFirstByInviteCode(String inviteCode);
    void deleteByInviteCode(String inviteCode);
}
