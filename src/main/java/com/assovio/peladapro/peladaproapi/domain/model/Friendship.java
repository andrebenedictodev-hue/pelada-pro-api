package com.assovio.peladapro.peladaproapi.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Friendship {

    private UUID id;
    private UUID userId;
    private UUID friendUserId;
    private OffsetDateTime createdAt;

    public Friendship() {
    }

    public Friendship(UUID id, UUID userId, UUID friendUserId, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.friendUserId = friendUserId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(UUID friendUserId) {
        this.friendUserId = friendUserId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
