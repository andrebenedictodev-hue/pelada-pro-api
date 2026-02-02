package com.assovio.peladapro.peladaproapi.infra.repository;

import com.assovio.peladapro.peladaproapi.domain.model.Invite;
import com.assovio.peladapro.peladaproapi.domain.repository.InviteRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryInviteRepository implements InviteRepository {

    private final ConcurrentHashMap<String, Invite> store = new ConcurrentHashMap<>();

    @Override
    public Invite save(Invite invite) {
        store.put(invite.getInviteCode(), invite);
        return invite;
    }

    @Override
    public Optional<Invite> findByInviteCode(String inviteCode) {
        return Optional.ofNullable(store.get(inviteCode));
    }

    @Override
    public void deleteByInviteCode(String inviteCode) {
        store.remove(inviteCode);
    }
}
