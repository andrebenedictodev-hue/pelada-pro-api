package com.assovio.peladapro.peladaproapi.api.model.output;

public class AuthOutput {

    private String token;
    private UserProfileOutput profile;
    private UserProfileOutput user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserProfileOutput getProfile() {
        return profile;
    }

    public void setProfile(UserProfileOutput profile) {
        this.profile = profile;
    }

    public UserProfileOutput getUser() {
        return user;
    }

    public void setUser(UserProfileOutput user) {
        this.user = user;
    }
}
