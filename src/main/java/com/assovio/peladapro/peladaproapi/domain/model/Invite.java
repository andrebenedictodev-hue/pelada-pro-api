package com.assovio.peladapro.peladaproapi.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Invite {

    private UUID id;
    private UUID eventId;
    private String inviteCode;
    private OffsetDateTime createdAt;

    public Invite() {
    }

    public Invite(UUID id, UUID eventId, String inviteCode, OffsetDateTime createdAt) {
        this.id = id;
        this.eventId = eventId;
        this.inviteCode = inviteCode;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
