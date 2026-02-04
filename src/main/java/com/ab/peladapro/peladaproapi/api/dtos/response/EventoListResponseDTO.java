package com.ab.peladapro.peladaproapi.api.dtos.response;

import java.util.List;

public class EventoListResponseDTO {

    private List<EventoResponseDTO> upcoming;
    private List<EventoResponseDTO> live;
    private List<EventoResponseDTO> finished;

    public List<EventoResponseDTO> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<EventoResponseDTO> upcoming) {
        this.upcoming = upcoming;
    }

    public List<EventoResponseDTO> getLive() {
        return live;
    }

    public void setLive(List<EventoResponseDTO> live) {
        this.live = live;
    }

    public List<EventoResponseDTO> getFinished() {
        return finished;
    }

    public void setFinished(List<EventoResponseDTO> finished) {
        this.finished = finished;
    }
}
