package com.ab.peladapro.peladaproapi.api.dtos.response;

public class AuthResponseDTO {

    private String token;
    private UsuarioProfileResponseDTO profile;
    private UsuarioProfileResponseDTO user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuarioProfileResponseDTO getProfile() {
        return profile;
    }

    public void setProfile(UsuarioProfileResponseDTO profile) {
        this.profile = profile;
    }

    public UsuarioProfileResponseDTO getUser() {
        return user;
    }

    public void setUser(UsuarioProfileResponseDTO user) {
        this.user = user;
    }
}
