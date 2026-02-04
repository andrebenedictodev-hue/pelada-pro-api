package com.ab.peladapro.peladaproapi.domain.service;

import com.ab.peladapro.peladaproapi.api.dtos.request.MatchEventRequestDTO;
import com.ab.peladapro.peladaproapi.domain.exception.NaoAutorizadoException;
import com.ab.peladapro.peladaproapi.domain.exception.NegocioException;
import com.ab.peladapro.peladaproapi.domain.model.Evento;
import com.ab.peladapro.peladaproapi.domain.model.MatchEvent;
import com.ab.peladapro.peladaproapi.domain.model.MatchEventType;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.dao.MatchEventDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MatchEventService {

    private final MatchEventDAO matchEventDAO;
    private final EventoService eventoService;
    private final RankingService rankingService;
    private final LiveService liveService;

    public MatchEventService(MatchEventDAO matchEventDAO,
            EventoService eventoService,
            RankingService rankingService,
            LiveService liveService) {
        this.matchEventDAO = matchEventDAO;
        this.eventoService = eventoService;
        this.rankingService = rankingService;
        this.liveService = liveService;
    }

    public MatchEvent create(UUID eventId, MatchEventRequestDTO input) {
        Evento evento = eventoService.getById(eventId);
        if (evento == null) {
            throw new NegocioException("Event not found");
        }

        MatchEvent event = new MatchEvent();
        event.setEventId(evento.getUuid());
        event.setPlayerId(input.getPlayerId().toString());
        event.setType(input.getType());
        event.setTeam(input.getTeam());
        event.setMatchTimeMs(input.getMatchTimeMs());

        matchEventDAO.save(event);
        rankingService.applyMatchEvent(event);

        if (input.getType() == MatchEventType.GOAL) {
            liveService.applyGoal(eventId, input.getTeam(), 1);
        }

        return event;
    }

    public List<MatchEvent> list(UUID eventId) {
        eventoService.getById(eventId);
        return matchEventDAO.findByEventId(eventId.toString());
    }

    public MatchEvent undoLast(UUID eventId, Usuario usuario) {
        Evento evento = eventoService.getById(eventId);
        if (!evento.getOwnerId().equals(usuario.getUuid())) {
            throw new NaoAutorizadoException("Only organizer can undo last event");
        }

        MatchEvent last = matchEventDAO.findFirstByEventIdOrderByCreatedAtDesc(evento.getUuid())
                .orElseThrow(() -> new NegocioException("No match events to undo"));
        matchEventDAO.delete(last);

        rankingService.undoMatchEvent(last);
        if (last.getType() == MatchEventType.GOAL) {
            liveService.applyGoal(eventId, last.getTeam(), -1);
        }

        return last;
    }
}
