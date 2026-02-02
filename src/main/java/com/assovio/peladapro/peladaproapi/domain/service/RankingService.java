package com.assovio.peladapro.peladaproapi.domain.service;

import com.assovio.peladapro.peladaproapi.domain.model.MatchEvent;
import com.assovio.peladapro.peladaproapi.domain.model.MatchEventType;
import com.assovio.peladapro.peladaproapi.domain.model.RankingEntry;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.repository.RankingRepository;
import com.assovio.peladapro.peladaproapi.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RankingService {

    private final RankingRepository rankingRepository;
    private final UsuarioRepository usuarioRepository;

    public RankingService(RankingRepository rankingRepository, UsuarioRepository usuarioRepository) {
        this.rankingRepository = rankingRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void applyMatchEvent(MatchEvent event) {
        RankingEntry global = getOrCreateGlobal(event.getPlayerId());
        RankingEntry perEvent = getOrCreateEvent(event.getEventId(), event.getPlayerId());
        applyDelta(global, event, 1);
        applyDelta(perEvent, event, 1);
        rankingRepository.upsertGlobal(global);
        rankingRepository.upsertByEvent(event.getEventId(), perEvent);
    }

    public void undoMatchEvent(MatchEvent event) {
        RankingEntry global = getOrCreateGlobal(event.getPlayerId());
        RankingEntry perEvent = getOrCreateEvent(event.getEventId(), event.getPlayerId());
        applyDelta(global, event, -1);
        applyDelta(perEvent, event, -1);
        rankingRepository.upsertGlobal(global);
        rankingRepository.upsertByEvent(event.getEventId(), perEvent);
    }

    public List<RankingEntry> getGlobal() {
        return rankingRepository.getGlobalRanking();
    }

    public List<RankingEntry> getByEvent(UUID eventId) {
        return rankingRepository.getEventRanking(eventId);
    }

    private RankingEntry getOrCreateGlobal(UUID userId) {
        RankingEntry entry = rankingRepository.getGlobalRanking().stream()
                .filter(item -> userId.equals(item.getUserId()))
                .findFirst()
                .orElse(null);
        if (entry == null) {
            entry = new RankingEntry(userId, resolveNickname(userId));
        }
        return entry;
    }

    private RankingEntry getOrCreateEvent(UUID eventId, UUID userId) {
        RankingEntry entry = rankingRepository.getEventRanking(eventId).stream()
                .filter(item -> userId.equals(item.getUserId()))
                .findFirst()
                .orElse(null);
        if (entry == null) {
            entry = new RankingEntry(userId, resolveNickname(userId));
        }
        return entry;
    }

    private String resolveNickname(UUID userId) {
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        return usuario != null ? usuario.getNickname() : "Unknown";
    }

    private void applyDelta(RankingEntry entry, MatchEvent event, int multiplier) {
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
}
