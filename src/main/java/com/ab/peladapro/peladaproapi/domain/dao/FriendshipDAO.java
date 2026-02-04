package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipDAO extends JpaRepository<Friendship, Long> {
    boolean existsByUserIdAndFriendUserId(String userId, String friendUserId);
    List<Friendship> findByUserId(String userId);
}
