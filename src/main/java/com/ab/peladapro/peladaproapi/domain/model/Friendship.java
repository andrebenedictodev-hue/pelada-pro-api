package com.ab.peladapro.peladaproapi.domain.model;

import com.ab.peladapro.peladaproapi.domain.model.contracts.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "friendship")
@SQLRestriction("deleted_at IS NULL")
public class Friendship extends EntityBase {

    @Column(name = "user_uuid", nullable = false)
    private String userId;

    @Column(name = "friend_user_uuid", nullable = false)
    private String friendUserId;

    public Friendship() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }
}
