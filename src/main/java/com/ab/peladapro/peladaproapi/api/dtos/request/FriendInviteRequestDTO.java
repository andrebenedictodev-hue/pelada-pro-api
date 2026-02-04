package com.ab.peladapro.peladaproapi.api.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class FriendInviteRequestDTO {

    @Email
    @NotBlank
    private String inviteeEmail;

    public String getInviteeEmail() {
        return inviteeEmail;
    }

    public void setInviteeEmail(String inviteeEmail) {
        this.inviteeEmail = inviteeEmail;
    }
}
