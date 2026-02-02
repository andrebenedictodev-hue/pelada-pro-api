package com.assovio.peladapro.peladaproapi.domain.repository;

import com.assovio.peladapro.peladaproapi.domain.model.Invite;

import java.util.Optional;

public interface InviteRepository {
    Invite save(Invite invite);
    Optional<Invite> findByInviteCode(String inviteCode);
    void deleteByInviteCode(String inviteCode);
}
