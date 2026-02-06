package com.ab.peladapro.peladaproapi.domain.service;

import com.ab.peladapro.peladaproapi.api.dtos.request.EventoRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.request.EventoSettingsUpdateRequestDTO;
import com.ab.peladapro.peladaproapi.domain.dao.EventoDAO;
import com.ab.peladapro.peladaproapi.domain.dao.LiveStateDAO;
import com.ab.peladapro.peladaproapi.domain.dao.MatchEventDAO;
import com.ab.peladapro.peladaproapi.domain.dao.ParticipanteDAO;
import com.ab.peladapro.peladaproapi.domain.dao.RankingEventDAO;
import com.ab.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException;
import com.ab.peladapro.peladaproapi.domain.exception.NegocioException;
import com.ab.peladapro.peladaproapi.domain.model.Evento;
import com.ab.peladapro.peladaproapi.domain.model.EventoSettings;
import com.ab.peladapro.peladaproapi.domain.model.EventoStatus;
import com.ab.peladapro.peladaproapi.domain.model.Participante;
import com.ab.peladapro.peladaproapi.domain.model.ParticipanteRole;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoDAO eventoDAO;
    private final ParticipanteDAO participanteDAO;
    private final LiveService liveService;
    private final MatchEventDAO matchEventDAO;
    private final RankingEventDAO rankingEventDAO;
    private final LiveStateDAO liveStateDAO;

    public EventoService(EventoDAO eventoDAO,
            ParticipanteDAO participanteDAO,
            LiveService liveService,
            MatchEventDAO matchEventDAO,
            RankingEventDAO rankingEventDAO,
            LiveStateDAO liveStateDAO) {
        this.eventoDAO = eventoDAO;
        this.participanteDAO = participanteDAO;
        this.liveService = liveService;
        this.matchEventDAO = matchEventDAO;
        this.rankingEventDAO = rankingEventDAO;
        this.liveStateDAO = liveStateDAO;
    }

    public Evento create(EventoRequestDTO input, Usuario owner) {
        String rawTitle = input.getTitle() != null ? input.getTitle().trim() : "";
        String rawLocation = input.getLocation() != null ? input.getLocation().trim() : "";
        if (rawTitle.isBlank() && rawLocation.isBlank()) {
            throw new NegocioException("Event title is required");
        }
        String title = rawTitle.isBlank() ? rawLocation : rawTitle;
        String location = rawLocation.isBlank() ? null : rawLocation;

        EventoSettings settings = new EventoSettings(
                input.getSettings().getTeamSize(),
                input.getSettings().getTimerDurationSec(),
                input.getSettings().getGoalsLimit(),
                input.getSettings().getMode());

        Evento evento = new Evento();
        evento.setOwnerId(owner.getUuid());
        evento.setTitle(title);
        evento.setLocation(location);
        evento.setType(input.getType());
        evento.setStatus(EventoStatus.UPCOMING);
        evento.setMaxPlayers(input.getMaxPlayers());
        evento.setSettings(settings);

        eventoDAO.save(evento);

        Participante organizer = new Participante();
        organizer.setEventId(evento.getUuid());
        organizer.setUserId(owner.getUuid());
        organizer.setRole(ParticipanteRole.ORGANIZER);
        organizer.setJoinedAt(OffsetDateTime.now());
        participanteDAO.save(organizer);

        return evento;
    }

    public Evento getById(UUID eventId) {
        return eventoDAO.findFirstByUuid(eventId.toString())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Event not found"));
    }

    public List<Evento> listAll() {
        return eventoDAO.findAll();
    }

    public Participante join(UUID eventId, Usuario usuario) {
        Evento evento = getById(eventId);

        if (participanteDAO.findFirstByEventIdAndUserId(evento.getUuid(), usuario.getUuid()).isPresent()) {
            throw new NegocioException("User already joined");
        }

        long count = participanteDAO.countByEventId(evento.getUuid());
        if (count >= evento.getMaxPlayers()) {
            throw new NegocioException("Max players reached");
        }

        Participante participante = new Participante();
        participante.setEventId(evento.getUuid());
        participante.setUserId(usuario.getUuid());
        participante.setRole(ParticipanteRole.PLAYER);
        participante.setJoinedAt(OffsetDateTime.now());
        participanteDAO.save(participante);

        return participante;
    }

    public void leave(UUID eventId, Usuario usuario) {
        Evento evento = getById(eventId);
        if (evento.getOwnerId().equals(usuario.getUuid())) {
            throw new NegocioException("Organizer cannot leave the event");
        }

        Participante participante = participanteDAO.findFirstByEventIdAndUserId(evento.getUuid(), usuario.getUuid())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Participant not found"));

        participanteDAO.deleteById(participante.getId());
    }

    public void deleteEvent(UUID eventId, Usuario usuario) {
        Evento evento = getById(eventId);
        if (!evento.getOwnerId().equals(usuario.getUuid())) {
            throw new NegocioException("Only organizer can delete event");
        }
        participanteDAO.deleteByEventId(evento.getUuid());
        matchEventDAO.deleteByEventId(evento.getUuid());
        rankingEventDAO.deleteByEventId(evento.getUuid());
        liveStateDAO.deleteByEventId(evento.getUuid());
        eventoDAO.delete(evento);
    }

    public Evento updateSettings(UUID eventId, Usuario organizer, EventoSettingsUpdateRequestDTO input) {
        if (organizer == null) {
            throw new NegocioException("Not authorized");
        }
        Evento evento = getById(eventId);
        if (!evento.getOwnerId().equals(organizer.getUuid())) {
            throw new NegocioException("Only organizer can update event settings");
        }
        EventoSettings settings = evento.getSettings();
        if (settings == null) {
            throw new NegocioException("Event settings not found");
        }

        boolean changed = false;
        if (input.getTimerDurationSec() != null) {
            settings.setTimerDurationSec(input.getTimerDurationSec());
            changed = true;
        }
        if (input.getGoalsLimit() != null) {
            settings.setGoalsLimit(input.getGoalsLimit());
            changed = true;
        }
        if (!changed) {
            throw new NegocioException("No settings provided to update");
        }

        evento.setSettings(settings);
        return eventoDAO.save(evento);
    }

    public void removeParticipant(UUID eventId, UUID targetUserId, Usuario organizer) {
        Evento evento = getById(eventId);
        if (!evento.getOwnerId().equals(organizer.getUuid())) {
            throw new NegocioException("Only organizer can remove participants");
        }
        Participante participante = participanteDAO.findFirstByEventIdAndUserId(evento.getUuid(), targetUserId.toString())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Participant not found"));
        participanteDAO.deleteById(participante.getId());
        liveService.removePlayerFromTeams(eventId, targetUserId);
    }

    public List<Participante> listParticipants(UUID eventId) {
        getById(eventId);
        return participanteDAO.findByEventId(eventId.toString());
    }

    public long countParticipants(UUID eventId) {
        return participanteDAO.countByEventId(eventId.toString());
    }

    public boolean hasOwnerEvents(UUID ownerId) {
        return !eventoDAO.findByOwnerId(ownerId.toString()).isEmpty();
    }

}
