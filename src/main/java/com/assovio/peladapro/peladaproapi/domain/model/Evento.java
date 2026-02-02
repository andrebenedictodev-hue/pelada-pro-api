package com.assovio.peladapro.peladaproapi.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Evento {

    private UUID id;
    private UUID ownerId;
    private String title;
    private String location;
    private EventoType type;
    private EventoStatus status;
    private int maxPlayers;
    private String inviteCode;
    private EventoSettings settings;
    private OffsetDateTime createdAt;

    public Evento() {
    }

    public Evento(UUID id, UUID ownerId, String title, String location, EventoType type, EventoStatus status,
            int maxPlayers,
            String inviteCode, EventoSettings settings, OffsetDateTime createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.location = location;
        this.type = type;
        this.status = status;
        this.maxPlayers = maxPlayers;
        this.inviteCode = inviteCode;
        this.settings = settings;
        this.createdAt = createdAt;
    }

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

    public EventoSettings getSettings() {
        return settings;
    }

    public void setSettings(EventoSettings settings) {
        this.settings = settings;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
