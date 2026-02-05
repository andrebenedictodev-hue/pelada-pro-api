package com.ab.peladapro.peladaproapi.domain.service;

import com.ab.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException;
import com.ab.peladapro.peladaproapi.domain.exception.NegocioException;
import com.ab.peladapro.peladaproapi.domain.model.Evento;
import com.ab.peladapro.peladaproapi.domain.model.EventoStatus;
import com.ab.peladapro.peladaproapi.domain.model.LiveState;
import com.ab.peladapro.peladaproapi.domain.model.LiveStatus;
import com.ab.peladapro.peladaproapi.domain.model.MatchTeam;
import com.ab.peladapro.peladaproapi.domain.model.Participante;
import com.ab.peladapro.peladaproapi.domain.dao.EventoDAO;
import com.ab.peladapro.peladaproapi.domain.dao.LiveStateDAO;
import com.ab.peladapro.peladaproapi.domain.dao.ParticipanteDAO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class LiveService {

    private final LiveStateDAO liveStateDAO;
    private final EventoDAO eventoDAO;
    private final ParticipanteDAO participanteDAO;

    public LiveService(LiveStateDAO liveStateDAO, EventoDAO eventoDAO,
            ParticipanteDAO participanteDAO) {
        this.liveStateDAO = liveStateDAO;
        this.eventoDAO = eventoDAO;
        this.participanteDAO = participanteDAO;
    }

    public LiveState getState(UUID eventId) {
        LiveState state = liveStateDAO.findFirstByEventId(eventId.toString()).orElseGet(() -> {
            LiveState newState = new LiveState();
            newState.setEventId(eventId.toString());
            newState.setStatus(LiveStatus.IDLE);
            newState.setAccumulatedTimeMs(0);
            newState.setStartedAtServerEpochMs(0);
            newState.setScoreA(0);
            newState.setScoreB(0);
            return liveStateDAO.save(newState);
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
        ensureTeamsReady(eventId, state);
        if (state.getStatus() == LiveStatus.RUNNING) {
            updateEventoStatus(eventId, EventoStatus.LIVE);
            return state;
        }
        state.setStatus(LiveStatus.RUNNING);
        state.setStartedAtServerEpochMs(System.currentTimeMillis());
        updateEventoStatus(eventId, EventoStatus.LIVE);
        return liveStateDAO.save(state);
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
        return liveStateDAO.save(state);
    }

    public LiveState reset(UUID eventId) {
        LiveState state = getState(eventId);
        state.setStatus(LiveStatus.IDLE);
        state.setAccumulatedTimeMs(0);
        state.setStartedAtServerEpochMs(0);
        state.setScoreA(0);
        state.setScoreB(0);
        state.getTeams().clear();
        state.getQueue().clear();
        updateEventoStatus(eventId, EventoStatus.UPCOMING);
        return liveStateDAO.save(state);
    }

    public LiveState shuffleTeams(UUID eventId) {
        LiveState state = getState(eventId);
        Evento evento = getEventoOrThrow(eventId);
        List<UUID> playerIds = listParticipantUserIds(eventId);
        int teamSize = evento.getSettings().getTeamSize();
        int teamCount = playerIds.size() / teamSize;
        if (teamCount < 2) {
            throw new NegocioException("É necessário no mínimo dois times completos para sortear");
        }
        Collections.shuffle(playerIds);
        List<List<UUID>> teams = new ArrayList<>();
        for (int i = 0; i < teamCount; i++) {
            int start = i * teamSize;
            teams.add(new ArrayList<>(playerIds.subList(start, start + teamSize)));
        }
        List<UUID> queue = new ArrayList<>();
        int assigned = teamCount * teamSize;
        if (playerIds.size() > assigned) {
            queue.addAll(playerIds.subList(assigned, playerIds.size()));
        }

        String ownerId = evento.getOwnerId();
        UUID ownerUuid = ownerId != null ? UUID.fromString(ownerId) : null;
        if (ownerUuid != null && queue.contains(ownerUuid)) {
            Random random = new Random();
            int teamIndex = random.nextInt(teams.size());
            List<UUID> targetTeam = teams.get(teamIndex);
            if (!targetTeam.isEmpty()) {
                int index = random.nextInt(targetTeam.size());
                UUID swapped = targetTeam.get(index);
                targetTeam.set(index, ownerUuid);
                queue.remove(ownerUuid);
                queue.add(swapped);
            }
        }
        ensureLeadersInTeams(eventId, state.getLeaders(), teams, queue);
        state.setTeams(teams);
        state.setQueue(queue);
        return liveStateDAO.save(state);
    }

    public LiveState setTeams(UUID eventId, List<List<UUID>> teams, List<UUID> queue) {
        LiveState state = getState(eventId);
        List<List<UUID>> safeTeams = teams != null ? teams : List.of();
        List<UUID> safeQueue = queue != null ? queue : List.of();
        validateTeams(eventId, safeTeams, safeQueue);
        List<List<UUID>> normalized = new ArrayList<>();
        for (List<UUID> team : safeTeams) {
            normalized.add(new ArrayList<>(team != null ? team : List.of()));
        }
        ensureLeadersInTeams(eventId, state.getLeaders(), normalized, safeQueue);
        state.setTeams(normalized);
        state.setQueue(new ArrayList<>(safeQueue));
        return liveStateDAO.save(state);
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
        return liveStateDAO.save(state);
    }

    public void applyGoal(UUID eventId, MatchTeam team, int delta) {
        LiveState state = getState(eventId);
        if (team == MatchTeam.A) {
            state.setScoreA(Math.max(0, state.getScoreA() + delta));
        } else {
            state.setScoreB(Math.max(0, state.getScoreB() + delta));
        }
        liveStateDAO.save(state);
    }

    public LiveState setLeaders(UUID eventId, List<UUID> leaders) {
        LiveState state = getState(eventId);
        List<UUID> safeLeaders = leaders != null ? leaders : List.of();
        if (safeLeaders.isEmpty()) {
            List<UUID> previousLeaders = state.getLeaders() != null ? new ArrayList<>(state.getLeaders()) : List.of();
            List<List<UUID>> teams = state.getTeams() != null ? state.getTeams() : new ArrayList<>();
            List<UUID> queue = state.getQueue() != null ? state.getQueue() : new ArrayList<>();
            if (!previousLeaders.isEmpty()) {
                for (UUID leaderId : previousLeaders) {
                    removeFromAll(teams, queue, leaderId);
                    if (!queue.contains(leaderId)) {
                        queue.add(leaderId);
                    }
                }
            }
            state.setLeaders(new ArrayList<>());
            state.setTeams(teams);
            state.setQueue(queue);
            return liveStateDAO.save(state);
        }
        Evento evento = getEventoOrThrow(eventId);
        int teamSize = evento.getSettings().getTeamSize();
        int teamCount = teamSize > 0 ? listParticipantUserIds(eventId).size() / teamSize : 0;
        if (teamCount < 2) {
            throw new NegocioException("Não há jogadores suficientes para definir líderes");
        }
        if (safeLeaders.size() != teamCount) {
            throw new NegocioException("Selecione exatamente " + teamCount + " líderes");
        }
        if (new HashSet<>(safeLeaders).size() != safeLeaders.size()) {
            throw new NegocioException("Líderes devem ser diferentes");
        }
        Set<UUID> allowed = new HashSet<>(listParticipantUserIds(eventId));
        for (UUID id : safeLeaders) {
            if (!allowed.contains(id)) {
                throw new NegocioException("Líder não faz parte do evento");
            }
        }
        state.setLeaders(new ArrayList<>(safeLeaders));
        List<List<UUID>> teams = state.getTeams() != null ? state.getTeams() : new ArrayList<>();
        while (teams.size() < 2) {
            teams.add(new ArrayList<>());
        }
        List<UUID> queue = state.getQueue() != null ? state.getQueue() : new ArrayList<>();
        ensureLeadersInTeams(eventId, safeLeaders, teams, queue);
        state.setTeams(teams);
        state.setQueue(queue);
        return liveStateDAO.save(state);
    }

    public LiveState pickPlayer(UUID eventId, int teamIndex, UUID userId) {
        LiveState state = getState(eventId);
        List<List<UUID>> teams = state.getTeams() != null ? state.getTeams() : new ArrayList<>();
        while (teams.size() <= teamIndex) {
            teams.add(new ArrayList<>());
        }
        validatePlayerForPick(eventId, teams, state.getQueue(), teamIndex, userId);
        removeFromAll(teams, state.getQueue(), userId);
        teams.get(teamIndex).add(userId);
        state.setTeams(teams);
        return liveStateDAO.save(state);
    }

    public LiveState removeFromTeam(UUID eventId, int teamIndex, UUID userId) {
        LiveState state = getState(eventId);
        List<List<UUID>> teams = state.getTeams() != null ? state.getTeams() : new ArrayList<>();
        if (teamIndex < 0 || teamIndex >= teams.size()) {
            throw new NegocioException("Time inválido");
        }
        List<UUID> team = teams.get(teamIndex);
        if (!team.remove(userId)) {
            return state;
        }
        List<UUID> queue = state.getQueue() != null ? state.getQueue() : new ArrayList<>();
        if (!queue.contains(userId)) {
            queue.add(userId);
        }
        state.setQueue(queue);
        state.setTeams(teams);
        return liveStateDAO.save(state);
    }

    public void removePlayerFromTeams(UUID eventId, UUID userId) {
        liveStateDAO.findFirstByEventId(eventId.toString()).ifPresent(state -> {
            if (state.getTeams() != null) {
                for (List<UUID> team : state.getTeams()) {
                    if (team != null) {
                        team.removeIf(id -> id.equals(userId));
                    }
                }
            }
            if (state.getQueue() != null) {
                state.getQueue().removeIf(id -> id.equals(userId));
            }
            liveStateDAO.save(state);
        });
    }

    private void ensureTeamsReady(UUID eventId, LiveState state) {
        Evento evento = getEventoOrThrow(eventId);
        int teamSize = evento.getSettings().getTeamSize();
        List<List<UUID>> teams = state.getTeams();
        if (teams == null || teams.size() < 2) {
            throw new NegocioException("Times devem ter pelo menos " + teamSize + " jogadores");
        }
        if (teams.get(0).size() < teamSize || teams.get(1).size() < teamSize) {
            throw new NegocioException("Times devem ter pelo menos " + teamSize + " jogadores");
        }
    }

    private void validateTeams(UUID eventId, List<List<UUID>> teams, List<UUID> queue) {
        Set<UUID> seen = new HashSet<>();
        for (List<UUID> team : teams) {
            if (!isUnique(team != null ? team : List.of(), seen)) {
                throw new NegocioException("Jogador não pode estar em mais de um time");
            }
        }
        if (!isUnique(queue, seen)) {
            throw new NegocioException("Jogador não pode estar em mais de um time");
        }
        Set<UUID> allowed = new HashSet<>(listParticipantUserIds(eventId));
        for (UUID id : seen) {
            if (!allowed.contains(id)) {
                throw new NegocioException("Jogador não faz parte deste evento");
            }
        }
    }

    private boolean isUnique(List<UUID> list, Set<UUID> seen) {
        for (UUID id : list) {
            if (!seen.add(id)) return false;
        }
        return true;
    }

    private void validatePlayerForPick(UUID eventId, List<List<UUID>> teams, List<UUID> queue, int teamIndex, UUID userId) {
        Evento evento = getEventoOrThrow(eventId);
        int teamSize = evento.getSettings().getTeamSize();
        if (teamIndex < 0 || teamIndex >= teams.size()) {
            throw new NegocioException("Time inválido");
        }
        Set<UUID> allowed = new HashSet<>(listParticipantUserIds(eventId));
        if (!allowed.contains(userId)) {
            throw new NegocioException("Jogador não faz parte deste evento");
        }
        List<UUID> team = teams.get(teamIndex);
        if (team.size() >= teamSize) {
            throw new NegocioException("Time já está completo");
        }
        if (queue == null || !queue.contains(userId)) {
            throw new NegocioException("Jogador não está na fila");
        }
    }

    private void removeFromAll(List<List<UUID>> teams, List<UUID> queue, UUID userId) {
        if (teams != null) {
            for (List<UUID> team : teams) {
                if (team != null) {
                    team.removeIf(id -> id.equals(userId));
                }
            }
        }
        if (queue != null) {
            queue.removeIf(id -> id.equals(userId));
        }
    }

    private void ensureLeadersInTeams(UUID eventId, List<UUID> leaders, List<List<UUID>> teams, List<UUID> queue) {
        if (leaders == null || leaders.isEmpty()) {
            return;
        }
        while (teams.size() < leaders.size()) {
            teams.add(new ArrayList<>());
        }
        for (int i = 0; i < leaders.size(); i++) {
            UUID leaderId = leaders.get(i);
            removeFromAll(teams, queue, leaderId);
            teams.get(i).add(leaderId);
        }
    }

    private List<UUID> listParticipantUserIds(UUID eventId) {
        List<Participante> participantes = participanteDAO.findByEventId(eventId.toString());
        List<UUID> userIds = new ArrayList<>();
        for (Participante participante : participantes) {
            if (participante.getUserId() != null) {
                userIds.add(UUID.fromString(participante.getUserId()));
            }
        }
        return userIds;
    }

    private Evento getEventoOrThrow(UUID eventId) {
        return eventoDAO.findFirstByUuid(eventId.toString())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Event not found"));
    }

    private void updateEventoStatus(UUID eventId, EventoStatus status) {
        eventoDAO.findFirstByUuid(eventId.toString()).ifPresent(evento -> {
            if (evento.getStatus() != status) {
                evento.setStatus(status);
                eventoDAO.save(evento);
            }
        });
    }
}
