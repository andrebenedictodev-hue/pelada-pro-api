package com.ab.peladapro.peladaproapi.domain.model;

import com.ab.peladapro.peladaproapi.domain.model.contracts.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "evento")
@SQLRestriction("deleted_at IS NULL")
public class Evento extends EntityBase {

    @Column(name = "owner_uuid", nullable = false)
    private String ownerId;

    @Column(name = "title")
    private String title;

    @Column(name = "location", nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EventoType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EventoStatus status;

    @Column(name = "max_players", nullable = false)
    private int maxPlayers;

    @Column(name = "invite_code", nullable = false)
    private String inviteCode;

    @Embedded
    private EventoSettings settings;

    public Evento() {
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
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
}
