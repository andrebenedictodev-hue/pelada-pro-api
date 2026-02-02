package com.assovio.peladapro.peladaproapi.infra.repository;

import com.assovio.peladapro.peladaproapi.domain.repository.PendingInviteRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPendingInviteRepository implements PendingInviteRepository {

    private final ConcurrentHashMap<String, List<UUID>> store = new ConcurrentHashMap<>();

    @Override
    public void addInvite(String email, UUID inviterId) {
        store.computeIfAbsent(email.toLowerCase(), key -> new ArrayList<>()).add(inviterId);
    }

    @Override
    public List<UUID> getInviters(String email) {
        return new ArrayList<>(store.getOrDefault(email.toLowerCase(), List.of()));
    }

    @Override
    public void clearInviters(String email) {
        store.remove(email.toLowerCase());
    }
}
