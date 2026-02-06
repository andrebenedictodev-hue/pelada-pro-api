package com.ab.peladapro.peladaproapi.api.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class EventoSettingsUpdateRequestDTO {

    @Min(60)
    @Max(5400)
    private Integer timerDurationSec;

    @Min(1)
    @Max(50)
    private Integer goalsLimit;

    public Integer getTimerDurationSec() {
        return timerDurationSec;
    }

    public void setTimerDurationSec(Integer timerDurationSec) {
        this.timerDurationSec = timerDurationSec;
    }

    public Integer getGoalsLimit() {
        return goalsLimit;
    }

    public void setGoalsLimit(Integer goalsLimit) {
        this.goalsLimit = goalsLimit;
    }
}
