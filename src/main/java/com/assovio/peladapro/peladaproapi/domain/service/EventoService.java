package com.assovio.peladapro.peladaproapi.domain.service;

import com.assovio.peladapro.peladaproapi.api.model.input.EventCreateInput;
import com.assovio.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException;
import com.assovio.peladapro.peladaproapi.domain.exception.NegocioException;
import com.assovio.peladapro.peladaproapi.domain.model.Evento;
import com.assovio.peladapro.peladaproapi.domain.model.EventoSettings;
import com.assovio.peladapro.peladaproapi.domain.model.EventoStatus;
import com.assovio.peladapro.peladaproapi.domain.model.Invite;
import com.assovio.peladapro.peladaproapi.domain.model.Participante;
import com.assovio.peladapro.peladaproapi.domain.model.ParticipanteRole;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.repository.EventoRepository;
import com.assovio.peladapro.peladaproapi.domain.repository.InviteRepository;
import com.assovio.peladapro.peladaproapi.domain.repository.ParticipanteRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final ParticipanteRepository participanteRepository;
    private final InviteRepository inviteRepository;

    public EventoService(EventoRepository eventoRepository,
            ParticipanteRepository participanteRepository,
            InviteRepository inviteRepository) {
        this.eventoRepository = eventoRepository;
        this.participanteRepository = participanteRepository;
        this.inviteRepository = inviteRepository;
    }

    public Evento create(EventCreateInput input, Usuario owner) {
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
        evento.setId(UUID.randomUUID());
        evento.setOwnerId(owner.getId());
        evento.setTitle(title);
        evento.setLocation(location);
        evento.setType(input.getType());
        evento.setStatus(EventoStatus.UPCOMING);
        evento.setMaxPlayers(input.getMaxPlayers());
        evento.setInviteCode(generateInviteCode());
        evento.setSettings(settings);
        evento.setCreatedAt(OffsetDateTime.now());

        eventoRepository.save(evento);

        Invite invite = new Invite(UUID.randomUUID(), evento.getId(), evento.getInviteCode(), OffsetDateTime.now());
        inviteRepository.save(invite);

        Participante organizer = new Participante(UUID.randomUUID(), evento.getId(), owner.getId(), null,
                ParticipanteRole.ORGANIZER, OffsetDateTime.now());
        participanteRepository.save(organizer);

        return evento;
    }

    public Evento getById(UUID eventId) {
        return eventoRepository.findById(eventId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Event not found"));
    }

    public List<Evento> listAll() {
        return eventoRepository.findAll();
    }

    public Participante join(UUID eventId, Usuario usuario) {
        Evento evento = getById(eventId);

        if (participanteRepository.findByEventIdAndUserId(eventId, usuario.getId()).isPresent()) {
            throw new NegocioException("User already joined");
        }

        long count = participanteRepository.countByEventId(eventId);
        if (count >= evento.getMaxPlayers()) {
            throw new NegocioException("Max players reached");
        }

        Participante participante = new Participante(UUID.randomUUID(), eventId, usuario.getId(), null,
                ParticipanteRole.PLAYER, OffsetDateTime.now());
        participanteRepository.save(participante);

        return participante;
    }

    public void leave(UUID eventId, Usuario usuario) {
        Evento evento = getById(eventId);
        if (evento.getOwnerId().equals(usuario.getId())) {
            throw new NegocioException("Organizer cannot leave the event");
        }

        Participante participante = participanteRepository.findByEventIdAndUserId(eventId, usuario.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Participant not found"));

        participanteRepository.deleteById(participante.getId());
    }

    public List<Participante> listParticipants(UUID eventId) {
        getById(eventId);
        return participanteRepository.findByEventId(eventId);
    }

    public long countParticipants(UUID eventId) {
        return participanteRepository.countByEventId(eventId);
    }

    public boolean hasOwnerEvents(UUID ownerId) {
        return !eventoRepository.findByOwnerId(ownerId).isEmpty();
    }

    private String generateInviteCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
