package com.ab.peladapro.peladaproapi.api.dtos.response;

import java.util.UUID;

public class UsuarioSearchResponseDTO {

    private UUID id;
    private String nickname;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
