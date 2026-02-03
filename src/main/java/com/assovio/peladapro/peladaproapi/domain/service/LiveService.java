package com.assovio.peladapro.peladaproapi.domain.service;

import com.assovio.peladapro.peladaproapi.domain.model.Evento;
import com.assovio.peladapro.peladaproapi.domain.model.EventoStatus;
import com.assovio.peladapro.peladaproapi.domain.model.LiveState;
import com.assovio.peladapro.peladaproapi.domain.model.LiveStatus;
import com.assovio.peladapro.peladaproapi.domain.model.MatchTeam;
import com.assovio.peladapro.peladaproapi.domain.repository.EventoRepository;
import com.assovio.peladapro.peladaproapi.domain.repository.LiveStateRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LiveService {

    private final LiveStateRepository liveStateRepository;
    private final EventoRepository eventoRepository;

    public LiveService(LiveStateRepository liveStateRepository, EventoRepository eventoRepository) {
        this.liveStateRepository = liveStateRepository;
        this.eventoRepository = eventoRepository;
    }

    public LiveState getState(UUID eventId) {
        LiveState state = liveStateRepository.findByEventId(eventId).orElseGet(() -> {
            LiveState newState = new LiveState();
            newState.setStatus(LiveStatus.IDLE);
            newState.setAccumulatedTimeMs(0);
            newState.setStartedAtServerEpochMs(0);
            newState.setScoreA(0);
            newState.setScoreB(0);
            return liveStateRepository.save(eventId, newState);
        });
        if (state.getStatus() == LiveStatus.RUNNING) {
            updateEventoStatus(eventId, EventoStatus.LIVE);
        } else if (state.getStatus() == LiveStatus.ENDED) {
            updateEventoStatus(eventId, EventoStatus.FINISHED);
        }
        return state;
    }

    public LiveState start(UUID eventId) {
        LiveState state = getState(eventId);
        if (state.getStatus() == LiveStatus.RUNNING) {
            updateEventoStatus(eventId, EventoStatus.LIVE);
            return state;
        }
        state.setStatus(LiveStatus.RUNNING);
        state.setStartedAtServerEpochMs(System.currentTimeMillis());
        updateEventoStatus(eventId, EventoStatus.LIVE);
        return liveStateRepository.save(eventId, state);
    }

    public LiveState pause(UUID eventId) {
        LiveState state = getState(eventId);
        if (state.getStatus() != LiveStatus.RUNNING) {
            return state;
        }
        long now = System.currentTimeMillis();
        long elapsed = Math.max(0, now - state.getStartedAtServerEpochMs());
        state.setAccumulatedTimeMs(state.getAccumulatedTimeMs() + elapsed);
        state.setStatus(LiveStatus.PAUSED);
        state.setStartedAtServerEpochMs(0);
        return liveStateRepository.save(eventId, state);
    }

    public LiveState reset(UUID eventId) {
        LiveState state = getState(eventId);
        state.setStatus(LiveStatus.IDLE);
        state.setAccumulatedTimeMs(0);
        state.setStartedAtServerEpochMs(0);
        state.setScoreA(0);
        state.setScoreB(0);
        state.getCurrentTeamA().clear();
        state.getCurrentTeamB().clear();
        state.getQueue().clear();
        updateEventoStatus(eventId, EventoStatus.UPCOMING);
        return liveStateRepository.save(eventId, state);
    }

    public LiveState end(UUID eventId) {
        LiveState state = getState(eventId);
        if (state.getStatus() == LiveStatus.RUNNING) {
            long now = System.currentTimeMillis();
            long elapsed = Math.max(0, now - state.getStartedAtServerEpochMs());
            state.setAccumulatedTimeMs(state.getAccumulatedTimeMs() + elapsed);
        }
        state.setStatus(LiveStatus.ENDED);
        state.setStartedAtServerEpochMs(0);
        updateEventoStatus(eventId, EventoStatus.FINISHED);
        return liveStateRepository.save(eventId, state);
    }

    public void applyGoal(UUID eventId, MatchTeam team, int delta) {
        LiveState state = getState(eventId);
        if (team == MatchTeam.A) {
            state.setScoreA(Math.max(0, state.getScoreA() + delta));
        } else {
            state.setScoreB(Math.max(0, state.getScoreB() + delta));
        }
        liveStateRepository.save(eventId, state);
    }

    private void updateEventoStatus(UUID eventId, EventoStatus status) {
        eventoRepository.findById(eventId).ifPresent(evento -> {
            if (evento.getStatus() != status) {
                evento.setStatus(status);
                eventoRepository.save(evento);
            }
        });
    }
}
