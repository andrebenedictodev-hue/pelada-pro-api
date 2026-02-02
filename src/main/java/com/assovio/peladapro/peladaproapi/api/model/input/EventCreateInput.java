package com.assovio.peladapro.peladaproapi.api.model.input;

import com.assovio.peladapro.peladaproapi.domain.model.EventoType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EventCreateInput {

    private String title;

    private String location;

    @NotNull
    private EventoType type;

    @Min(2)
    private int maxPlayers;

    @Valid
    @NotNull
    private EventSettingsInput settings;

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

    public EventSettingsInput getSettings() {
        return settings;
    }

    public void setSettings(EventSettingsInput settings) {
        this.settings = settings;
    }
}
