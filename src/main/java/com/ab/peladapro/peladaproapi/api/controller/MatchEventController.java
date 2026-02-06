package com.ab.peladapro.peladaproapi.api.controller;

import com.ab.peladapro.peladaproapi.api.assembler.LiveStateAssembler;
import com.ab.peladapro.peladaproapi.api.assembler.MatchEventAssembler;
import com.ab.peladapro.peladaproapi.api.dtos.request.LiveLeadersRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.request.LiveNextMatchRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.request.LiveOvertimeRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.request.LiveTeamsRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.request.MatchEventRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.LiveStateResponseDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.MatchEventResponseDTO;
import com.ab.peladapro.peladaproapi.domain.exception.NegocioException;
import com.ab.peladapro.peladaproapi.domain.model.LiveState;
import com.ab.peladapro.peladaproapi.domain.model.LiveStatus;
import com.ab.peladapro.peladaproapi.domain.model.MatchEvent;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.service.LiveService;
import com.ab.peladapro.peladaproapi.domain.service.MatchEventService;
import com.ab.peladapro.peladaproapi.domain.service.RankingService;
import com.ab.peladapro.peladaproapi.domain.service.EventoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/events")
public class MatchEventController {

    private final MatchEventService matchEventService;
    private final LiveService liveService;
    private final RankingService rankingService;
    private final EventoService eventoService;
    private final MatchEventAssembler matchEventAssembler;
    private final LiveStateAssembler liveStateAssembler;

    @PostMapping("/{eventId}/match-events")
    public ResponseEntity<MatchEventResponseDTO> create(@PathVariable UUID eventId,
            @RequestBody @Valid MatchEventRequestDTO input) {
        MatchEvent event = matchEventService.create(eventId, input);
        return new ResponseEntity<>(matchEventAssembler.toDTO(event), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}/match-events")
    public ResponseEntity<List<MatchEventResponseDTO>> list(@PathVariable UUID eventId) {
        List<MatchEventResponseDTO> outputs = matchEventService.list(eventId).stream()
                .map(matchEventAssembler::toDTO)
                .toList();
        return new ResponseEntity<>(outputs, HttpStatus.OK);
    }

    @PostMapping("/{eventId}/match-events/undo-last")
    public ResponseEntity<MatchEventResponseDTO> undoLast(@PathVariable UUID eventId,
            @AuthenticationPrincipal Usuario usuario) {
        MatchEvent event = matchEventService.undoLast(eventId, usuario);
        return new ResponseEntity<>(matchEventAssembler.toDTO(event), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/live/state")
    public ResponseEntity<LiveStateResponseDTO> getState(@PathVariable UUID eventId) {
        LiveState state = liveService.getState(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/start")
    public ResponseEntity<LiveStateResponseDTO> start(@PathVariable UUID eventId) {
        LiveState state = liveService.start(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/pause")
    public ResponseEntity<LiveStateResponseDTO> pause(@PathVariable UUID eventId) {
        LiveState state = liveService.pause(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/reset")
    public ResponseEntity<LiveStateResponseDTO> reset(@PathVariable UUID eventId) {
        LiveState state = liveService.reset(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/end")
    public ResponseEntity<LiveStateResponseDTO> end(@PathVariable UUID eventId) {
        LiveState current = liveService.getState(eventId);
        if (current.getStatus() == LiveStatus.ENDED) {
            return new ResponseEntity<>(liveStateAssembler.toOutput(current), HttpStatus.OK);
        }
        LiveState state = liveService.end(eventId);
        List<MatchEvent> events = matchEventService.list(eventId);
        rankingService.applyMatchResult(eventId, state, events);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/next-match")
    public ResponseEntity<LiveStateResponseDTO> nextMatch(@PathVariable UUID eventId,
            @RequestBody(required = false) LiveNextMatchRequestDTO input,
            @AuthenticationPrincipal Usuario usuario) {
        authorizeOrganizer(eventId, usuario);
        LiveState state = liveService.nextMatch(eventId, input != null ? input.getOutgoingPlayerIds() : null);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/overtime")
    public ResponseEntity<LiveStateResponseDTO> addOvertime(@PathVariable UUID eventId,
            @RequestBody @Valid LiveOvertimeRequestDTO input,
            @AuthenticationPrincipal Usuario usuario) {
        authorizeOrganizer(eventId, usuario);
        LiveState state = liveService.addOvertime(eventId, input.getMinutes());
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/teams")
    public ResponseEntity<LiveStateResponseDTO> setTeams(@PathVariable UUID eventId,
            @RequestBody @Valid LiveTeamsRequestDTO input, @AuthenticationPrincipal Usuario usuario) {
        authorizeOrganizer(eventId, usuario);
        LiveState state = liveService.setTeams(eventId, input.getTeams(), input.getQueue());
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/teams/shuffle")
    public ResponseEntity<LiveStateResponseDTO> shuffleTeams(@PathVariable UUID eventId,
            @AuthenticationPrincipal Usuario usuario) {
        authorizeOrganizer(eventId, usuario);
        LiveState state = liveService.shuffleTeams(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/leaders")
    public ResponseEntity<LiveStateResponseDTO> setLeaders(@PathVariable UUID eventId,
            @RequestBody @Valid LiveLeadersRequestDTO input, @AuthenticationPrincipal Usuario usuario) {
        authorizeOrganizer(eventId, usuario);
        LiveState state = liveService.setLeaders(eventId, input.getLeaders());
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/teams/{teamIndex}/pick/{userId}")
    public ResponseEntity<LiveStateResponseDTO> pickPlayer(@PathVariable UUID eventId,
            @PathVariable int teamIndex, @PathVariable UUID userId,
            @AuthenticationPrincipal Usuario usuario) {
        authorizeOrganizerOrLeader(eventId, teamIndex, usuario);
        LiveState state = liveService.pickPlayer(eventId, teamIndex, userId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/teams/{teamIndex}/remove/{userId}")
    public ResponseEntity<LiveStateResponseDTO> removePlayer(@PathVariable UUID eventId,
            @PathVariable int teamIndex, @PathVariable UUID userId,
            @AuthenticationPrincipal Usuario usuario) {
        authorizeOrganizerOrLeader(eventId, teamIndex, usuario);
        LiveState state = liveService.removeFromTeam(eventId, teamIndex, userId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    private void authorizeOrganizer(UUID eventId, Usuario usuario) {
        if (usuario == null || !eventoService.getById(eventId).getOwnerId().equals(usuario.getUuid())) {
            throw new NegocioException("Somente o organizador pode realizar esta ação");
        }
    }

    private void authorizeOrganizerOrLeader(UUID eventId, int teamIndex, Usuario usuario) {
        if (usuario == null) {
            throw new NegocioException("Não autorizado");
        }
        if (eventoService.getById(eventId).getOwnerId().equals(usuario.getUuid())) {
            return;
        }
        LiveState state = liveService.getState(eventId);
        if (state.getLeaders() == null || state.getLeaders().size() < 2) {
            throw new NegocioException("Líderes não definidos");
        }
        if (teamIndex < 0 || teamIndex >= state.getLeaders().size()) {
            throw new NegocioException("Time inválido");
        }
        UUID leaderId = state.getLeaders().get(teamIndex);
        if (!UUID.fromString(usuario.getUuid()).equals(leaderId)) {
            throw new NegocioException("Não permitido");
        }
    }
}
