package com.assovio.peladapro.peladaproapi.api.model.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class FriendInviteInput {

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
