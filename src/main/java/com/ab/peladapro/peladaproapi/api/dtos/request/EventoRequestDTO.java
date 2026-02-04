package com.ab.peladapro.peladaproapi.api.dtos.request;

import com.ab.peladapro.peladaproapi.domain.model.EventoType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EventoRequestDTO {

    private String title;

    @NotBlank
    private String location;

    @NotNull
    private EventoType type;

    @Min(2)
    private int maxPlayers;

    @Valid
    @NotNull
    private EventoSettingsRequestDTO settings;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EventoType getType() {
        return type;
    }

    public void setType(EventoType type) {
        this.type = type;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public EventoSettingsRequestDTO getSettings() {
        return settings;
    }

    public void setSettings(EventoSettingsRequestDTO settings) {
        this.settings = settings;
    }
}
