package com.assovio.peladapro.peladaproapi.api.model.output;

import com.assovio.peladapro.peladaproapi.domain.model.EventoStatus;
import com.assovio.peladapro.peladaproapi.domain.model.EventoType;

import java.util.UUID;

public class InviteOutput {

    private UUID eventId;
    private String title;
    private String location;
    private EventoType type;
    private EventoStatus status;
    private long participantsCount;

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventoType getType() {
        return type;
    }

    public void setType(EventoType type) {
        this.type = type;
    }

    public EventoStatus getStatus() {
        return status;
    }

    public void setStatus(EventoStatus status) {
        this.status = status;
    }

    public long getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(long participantsCount) {
        this.participantsCount = participantsCount;
    }
}
