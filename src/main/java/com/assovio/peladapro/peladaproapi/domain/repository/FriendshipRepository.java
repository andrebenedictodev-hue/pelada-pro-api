package com.assovio.peladapro.peladaproapi.domain.repository;

import com.assovio.peladapro.peladaproapi.domain.model.Friendship;

import java.util.List;
import java.util.UUID;

public interface FriendshipRepository {
    Friendship save(Friendship friendship);
    boolean existsByUserAndFriend(UUID userId, UUID friendUserId);
    List<Friendship> findByUserId(UUID userId);
    List<Friendship> findAll();
}
