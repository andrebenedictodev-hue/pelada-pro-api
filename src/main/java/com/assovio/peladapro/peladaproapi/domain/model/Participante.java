package com.assovio.peladapro.peladaproapi.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Participante {

    private UUID id;
    private UUID eventId;
    private UUID userId;
    private String guestName;
    private ParticipanteRole role;
    private OffsetDateTime joinedAt;

    public Participante() {
    }

    public Participante(UUID id, UUID eventId, UUID userId, String guestName, ParticipanteRole role,
            OffsetDateTime joinedAt) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.guestName = guestName;
        this.role = role;
        this.joinedAt = joinedAt;
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
}
