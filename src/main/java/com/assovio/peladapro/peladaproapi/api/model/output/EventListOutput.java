package com.assovio.peladapro.peladaproapi.api.model.output;

import java.util.List;

public class EventListOutput {

    private List<EventOutput> upcoming;
    private List<EventOutput> live;
    private List<EventOutput> finished;

    public List<EventOutput> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<EventOutput> upcoming) {
        this.upcoming = upcoming;
    }

    public List<EventOutput> getLive() {
        return live;
    }

    public void setLive(List<EventOutput> live) {
        this.live = live;
    }

    public List<EventOutput> getFinished() {
        return finished;
    }

    public void setFinished(List<EventOutput> finished) {
        this.finished = finished;
    }
}
