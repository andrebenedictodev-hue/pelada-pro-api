package com.assovio.peladapro.peladaproapi.infra.repository;

import com.assovio.peladapro.peladaproapi.domain.model.Friendship;
import com.assovio.peladapro.peladaproapi.domain.repository.FriendshipRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryFriendshipRepository implements FriendshipRepository {

    private final ConcurrentHashMap<UUID, Friendship> store = new ConcurrentHashMap<>();

    @Override
    public Friendship save(Friendship friendship) {
        store.put(friendship.getId(), friendship);
        return friendship;
    }

    @Override
    public boolean existsByUserAndFriend(UUID userId, UUID friendUserId) {
        return store.values().stream()
                .anyMatch(f -> userId.equals(f.getUserId()) && friendUserId.equals(f.getFriendUserId()));
    }

    @Override
    public List<Friendship> findByUserId(UUID userId) {
        return store.values().stream()
                .filter(f -> userId.equals(f.getUserId()))
                .toList();
    }

    @Override
    public List<Friendship> findAll() {
        return new ArrayList<>(store.values());
    }
}
