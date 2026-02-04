package com.ab.peladapro.peladaproapi.domain.service;

import com.ab.peladapro.peladaproapi.domain.model.LiveState;
import com.ab.peladapro.peladaproapi.domain.model.MatchEvent;
import com.ab.peladapro.peladaproapi.domain.model.MatchEventType;
import com.ab.peladapro.peladaproapi.domain.model.MatchTeam;
import com.ab.peladapro.peladaproapi.domain.model.RankingEvent;
import com.ab.peladapro.peladaproapi.domain.model.RankingEntry;
import com.ab.peladapro.peladaproapi.domain.model.RankingGlobal;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.dao.RankingEventDAO;
import com.ab.peladapro.peladaproapi.domain.dao.RankingGlobalDAO;
import com.ab.peladapro.peladaproapi.domain.dao.UsuarioDAO;
import com.ab.peladapro.peladaproapi.domain.dao.ParticipanteDAO;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class RankingService {

    private final RankingGlobalDAO rankingGlobalDAO;
    private final RankingEventDAO rankingEventDAO;
    private final UsuarioDAO usuarioDAO;
    private final ParticipanteDAO participanteDAO;

    public RankingService(RankingGlobalDAO rankingGlobalDAO,
            RankingEventDAO rankingEventDAO,
            UsuarioDAO usuarioDAO,
            ParticipanteDAO participanteDAO) {
        this.rankingGlobalDAO = rankingGlobalDAO;
        this.rankingEventDAO = rankingEventDAO;
        this.usuarioDAO = usuarioDAO;
        this.participanteDAO = participanteDAO;
    }

    public void applyMatchEvent(MatchEvent event) {
        RankingGlobal global = getOrCreateGlobal(event.getPlayerId());
        RankingEvent perEvent = getOrCreateEvent(event.getEventId(), event.getPlayerId());
        applyDelta(global, event, 1);
        applyDelta(perEvent, event, 1);
        rankingGlobalDAO.save(global);
        rankingEventDAO.save(perEvent);
    }

    public void undoMatchEvent(MatchEvent event) {
        RankingGlobal global = getOrCreateGlobal(event.getPlayerId());
        RankingEvent perEvent = getOrCreateEvent(event.getEventId(), event.getPlayerId());
        applyDelta(global, event, -1);
        applyDelta(perEvent, event, -1);
        rankingGlobalDAO.save(global);
        rankingEventDAO.save(perEvent);
    }

    public List<RankingEntry> getGlobal() {
        Map<String, RankingGlobal> map = new LinkedHashMap<>();
        rankingGlobalDAO.findAll().forEach(entry -> map.put(entry.getUserId(), entry));
        usuarioDAO.findAll().forEach(user -> {
            if (!map.containsKey(user.getUuid())) {
                RankingGlobal entry = new RankingGlobal();
                entry.setUserId(user.getUuid());
                entry.setNickname(user.getNickname());
                rankingGlobalDAO.save(entry);
                map.put(user.getUuid(), entry);
            }
        });
        return rankingGlobalDAO.findAll().stream().map(this::toEntry).toList();
    }

    public List<RankingEntry> getByEvent(UUID eventId) {
        String eventKey = eventId.toString();
        Map<String, RankingEvent> map = new LinkedHashMap<>();
        rankingEventDAO.findByEventId(eventKey).forEach(entry -> map.put(entry.getUserId(), entry));
        participanteDAO.findByEventId(eventKey).forEach(participante -> {
            String userId = participante.getUserId();
            if (userId == null || map.containsKey(userId)) {
                return;
            }
            RankingEvent entry = new RankingEvent();
            entry.setEventId(eventKey);
            entry.setUserId(userId);
            entry.setNickname(resolveNickname(userId));
            rankingEventDAO.save(entry);
            map.put(userId, entry);
        });
        return rankingEventDAO.findByEventId(eventKey).stream().map(this::toEntry).toList();
    }

    public void applyMatchResult(UUID eventId, LiveState state, List<MatchEvent> events) {
        List<List<UUID>> teams = state.getTeams();
        Set<UUID> teamA = new LinkedHashSet<>();
        Set<UUID> teamB = new LinkedHashSet<>();
        if (teams != null && teams.size() >= 2) {
            teamA.addAll(teams.get(0));
            teamB.addAll(teams.get(1));
        }

        if (teamA.isEmpty() && teamB.isEmpty()) {
            Map<UUID, MatchTeam> inferred = new LinkedHashMap<>();
            for (MatchEvent event : events) {
                inferred.put(UUID.fromString(event.getPlayerId()), event.getTeam());
            }
            for (Map.Entry<UUID, MatchTeam> entry : inferred.entrySet()) {
                if (entry.getValue() == MatchTeam.A) {
                    teamA.add(entry.getKey());
                } else {
                    teamB.add(entry.getKey());
                }
            }
        }

        if (teamA.isEmpty() && teamB.isEmpty()) {
            return;
        }

        int scoreA = state.getScoreA();
        int scoreB = state.getScoreB();
        boolean draw = scoreA == scoreB;
        boolean teamAWon = scoreA > scoreB;

        for (UUID playerId : teamA) {
            RankingGlobal global = getOrCreateGlobal(playerId.toString());
            RankingEvent perEvent = getOrCreateEvent(eventId.toString(), playerId.toString());
            applyMatchStats(global, draw, teamAWon);
            applyMatchStats(perEvent, draw, teamAWon);
            rankingGlobalDAO.save(global);
            rankingEventDAO.save(perEvent);
        }

        for (UUID playerId : teamB) {
            RankingGlobal global = getOrCreateGlobal(playerId.toString());
            RankingEvent perEvent = getOrCreateEvent(eventId.toString(), playerId.toString());
            applyMatchStats(global, draw, !teamAWon);
            applyMatchStats(perEvent, draw, !teamAWon);
            rankingGlobalDAO.save(global);
            rankingEventDAO.save(perEvent);
        }
    }

    private RankingGlobal getOrCreateGlobal(String userId) {
        RankingGlobal entry = rankingGlobalDAO.findFirstByUserId(userId).orElse(null);
        if (entry == null) {
            entry = new RankingGlobal();
            entry.setUserId(userId);
            entry.setNickname(resolveNickname(userId));
        }
        return entry;
    }

    private RankingEvent getOrCreateEvent(String eventId, String userId) {
        RankingEvent entry = rankingEventDAO.findFirstByEventIdAndUserId(eventId, userId).orElse(null);
        if (entry == null) {
            entry = new RankingEvent();
            entry.setEventId(eventId);
            entry.setUserId(userId);
            entry.setNickname(resolveNickname(userId));
        }
        return entry;
    }

    private String resolveNickname(String userId) {
        Usuario usuario = usuarioDAO.findFirstByUuid(userId).orElse(null);
        return usuario != null ? usuario.getNickname() : "Unknown";
    }

    private void applyDelta(RankingGlobal entry, MatchEvent event, int multiplier) {
        MatchEventType type = event.getType();
        if (type == MatchEventType.GOAL) {
            entry.setGoals(entry.getGoals() + (1 * multiplier));
            entry.setPoints(entry.getPoints() + (3 * multiplier));
        } else if (type == MatchEventType.ASSIST) {
            entry.setAssists(entry.getAssists() + (1 * multiplier));
            entry.setPoints(entry.getPoints() + (1 * multiplier));
        } else if (type == MatchEventType.CARD) {
            entry.setCards(entry.getCards() + (1 * multiplier));
            entry.setPoints(entry.getPoints() + (-1 * multiplier));
        } else {
            entry.setPoints(entry.getPoints() + (0 * multiplier));
        }
    }

    private void applyDelta(RankingEvent entry, MatchEvent event, int multiplier) {
        MatchEventType type = event.getType();
        if (type == MatchEventType.GOAL) {
            entry.setGoals(entry.getGoals() + (1 * multiplier));
            entry.setPoints(entry.getPoints() + (3 * multiplier));
        } else if (type == MatchEventType.ASSIST) {
            entry.setAssists(entry.getAssists() + (1 * multiplier));
            entry.setPoints(entry.getPoints() + (1 * multiplier));
        } else if (type == MatchEventType.CARD) {
            entry.setCards(entry.getCards() + (1 * multiplier));
            entry.setPoints(entry.getPoints() + (-1 * multiplier));
        } else {
            entry.setPoints(entry.getPoints() + (0 * multiplier));
        }
    }

    private void applyMatchStats(RankingGlobal entry, boolean draw, boolean won) {
        entry.setMatches(entry.getMatches() + 1);
        if (draw) {
            return;
        }
        if (won) {
            entry.setWins(entry.getWins() + 1);
        } else {
            entry.setLosses(entry.getLosses() + 1);
        }
    }

    private void applyMatchStats(RankingEvent entry, boolean draw, boolean won) {
        entry.setMatches(entry.getMatches() + 1);
        if (draw) {
            return;
        }
        if (won) {
            entry.setWins(entry.getWins() + 1);
        } else {
            entry.setLosses(entry.getLosses() + 1);
        }
    }

    private RankingEntry toEntry(RankingGlobal entry) {
        RankingEntry output = new RankingEntry(entry.getUserId(), entry.getNickname());
        output.setGoals(entry.getGoals());
        output.setAssists(entry.getAssists());
        output.setCards(entry.getCards());
        output.setPoints(entry.getPoints());
        output.setMatches(entry.getMatches());
        output.setWins(entry.getWins());
        output.setLosses(entry.getLosses());
        return output;
    }

    private RankingEntry toEntry(RankingEvent entry) {
        RankingEntry output = new RankingEntry(entry.getUserId(), entry.getNickname());
        output.setGoals(entry.getGoals());
        output.setAssists(entry.getAssists());
        output.setCards(entry.getCards());
        output.setPoints(entry.getPoints());
        output.setMatches(entry.getMatches());
        output.setWins(entry.getWins());
        output.setLosses(entry.getLosses());
        return output;
    }
}
