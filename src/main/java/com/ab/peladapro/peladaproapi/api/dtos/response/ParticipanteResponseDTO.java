package com.ab.peladapro.peladaproapi.api.dtos.response;

import com.ab.peladapro.peladaproapi.domain.model.ParticipanteRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ParticipanteResponseDTO {

    private UUID id;
    private UUID eventId;
    private UUID userId;
    private String guestName;
    private ParticipanteRole role;
    private OffsetDateTime joinedAt;
    private String displayName;

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public ParticipanteRole getRole() {
        return role;
    }

    public void setRole(ParticipanteRole role) {
        this.role = role;
    }

    public OffsetDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(OffsetDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
