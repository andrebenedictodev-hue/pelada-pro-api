package com.ab.peladapro.peladaproapi.domain.model;

import com.ab.peladapro.peladaproapi.domain.model.contracts.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "pending_invite")
@SQLRestriction("deleted_at IS NULL")
public class PendingInvite extends EntityBase {

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "inviter_uuid", nullable = false)
    private String inviterId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }
}
