package com.ab.peladapro.peladaproapi.domain.model;

import com.ab.peladapro.peladaproapi.domain.model.contracts.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;

@Entity
@Table(name = "participante")
@SQLRestriction("deleted_at IS NULL")
public class Participante extends EntityBase {

    @Column(name = "evento_uuid", nullable = false)
    private String eventId;

    @Column(name = "usuario_uuid")
    private String userId;

    @Column(name = "nome_convidado")
    private String guestName;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false)
    private ParticipanteRole role;

    @Column(name = "entrou_em", nullable = false)
    private OffsetDateTime joinedAt;

    public Participante() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
