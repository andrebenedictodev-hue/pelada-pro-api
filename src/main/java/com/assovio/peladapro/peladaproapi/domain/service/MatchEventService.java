package com.assovio.peladapro.peladaproapi.domain.service;

import com.assovio.peladapro.peladaproapi.api.model.input.MatchEventInput;
import com.assovio.peladapro.peladaproapi.domain.exception.NaoAutorizadoException;
import com.assovio.peladapro.peladaproapi.domain.exception.NegocioException;
import com.assovio.peladapro.peladaproapi.domain.model.Evento;
import com.assovio.peladapro.peladaproapi.domain.model.MatchEvent;
import com.assovio.peladapro.peladaproapi.domain.model.MatchEventType;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.repository.MatchEventRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MatchEventService {

    private final MatchEventRepository matchEventRepository;
    private final EventoService eventoService;
    private final RankingService rankingService;
    private final LiveService liveService;

    public MatchEventService(MatchEventRepository matchEventRepository,
            EventoService eventoService,
            RankingService rankingService,
            LiveService liveService) {
        this.matchEventRepository = matchEventRepository;
        this.eventoService = eventoService;
        this.rankingService = rankingService;
        this.liveService = liveService;
    }

    public MatchEvent create(UUID eventId, MatchEventInput input) {
        Evento evento = eventoService.getById(eventId);
        if (evento == null) {
            throw new NegocioException("Event not found");
        }

        MatchEvent event = new MatchEvent();
        event.setId(UUID.randomUUID());
        event.setEventId(eventId);
        event.setPlayerId(input.getPlayerId());
        event.setType(input.getType());
        event.setTeam(input.getTeam());
        event.setMatchTimeMs(input.getMatchTimeMs());
        event.setCreatedAt(OffsetDateTime.now());

        matchEventRepository.save(event);
        rankingService.applyMatchEvent(event);

        if (input.getType() == MatchEventType.GOAL) {
            liveService.applyGoal(eventId, input.getTeam(), 1);
        }

        return event;
    }

    public List<MatchEvent> list(UUID eventId) {
        eventoService.getById(eventId);
        return matchEventRepository.findByEventId(eventId);
    }

    public MatchEvent undoLast(UUID eventId, Usuario usuario) {
        Evento evento = eventoService.getById(eventId);
        if (!evento.getOwnerId().equals(usuario.getId())) {
            throw new NaoAutorizadoException("Only organizer can undo last event");
        }

        MatchEvent last = matchEventRepository.deleteLastByEventId(eventId)
                .orElseThrow(() -> new NegocioException("No match events to undo"));

        rankingService.undoMatchEvent(last);
        if (last.getType() == MatchEventType.GOAL) {
            liveService.applyGoal(eventId, last.getTeam(), -1);
        }

        return last;
    }
}
