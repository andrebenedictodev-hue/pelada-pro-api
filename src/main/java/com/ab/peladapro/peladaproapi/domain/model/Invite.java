package com.ab.peladapro.peladaproapi.domain.model;

import com.ab.peladapro.peladaproapi.domain.model.contracts.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "invite")
@SQLRestriction("deleted_at IS NULL")
public class Invite extends EntityBase {

    @Column(name = "event_uuid", nullable = false)
    private String eventId;

    @Column(name = "invite_code", nullable = false)
    private String inviteCode;

    public Invite() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
