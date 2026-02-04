package com.ab.peladapro.peladaproapi.api.dtos.response;

import com.ab.peladapro.peladaproapi.domain.model.EventoStatus;
import com.ab.peladapro.peladaproapi.domain.model.EventoType;

import java.time.OffsetDateTime;
import java.util.UUID;

public class EventoResponseDTO {

    private UUID id;
    private UUID ownerId;
    private String title;
    private String location;
    private EventoType type;
    private EventoStatus status;
    private int maxPlayers;
    private String inviteCode;
    private EventoSettingsResponseDTO settings;
    private OffsetDateTime createdAt;
    private long participantsCount;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventoType getType() {
        return type;
    }

    public void setType(EventoType type) {
        this.type = type;
    }

    public EventoStatus getStatus() {
        return status;
    }

    public void setStatus(EventoStatus status) {
        this.status = status;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public EventoSettingsResponseDTO getSettings() {
        return settings;
    }

    public void setSettings(EventoSettingsResponseDTO settings) {
        this.settings = settings;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(long participantsCount) {
        this.participantsCount = participantsCount;
    }
}
