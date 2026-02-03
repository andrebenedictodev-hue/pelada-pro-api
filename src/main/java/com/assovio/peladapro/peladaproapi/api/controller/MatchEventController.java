package com.assovio.peladapro.peladaproapi.api.controller;

import com.assovio.peladapro.peladaproapi.api.assembler.LiveStateAssembler;
import com.assovio.peladapro.peladaproapi.api.assembler.MatchEventAssembler;
import com.assovio.peladapro.peladaproapi.api.model.input.MatchEventInput;
import com.assovio.peladapro.peladaproapi.api.model.output.LiveStateOutput;
import com.assovio.peladapro.peladaproapi.api.model.output.MatchEventOutput;
import com.assovio.peladapro.peladaproapi.domain.model.LiveState;
import com.assovio.peladapro.peladaproapi.domain.model.MatchEvent;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.service.LiveService;
import com.assovio.peladapro.peladaproapi.domain.service.MatchEventService;
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
    private final MatchEventAssembler matchEventAssembler;
    private final LiveStateAssembler liveStateAssembler;

    @PostMapping("/{eventId}/match-events")
    public ResponseEntity<MatchEventOutput> create(@PathVariable UUID eventId,
            @RequestBody @Valid MatchEventInput input) {
        MatchEvent event = matchEventService.create(eventId, input);
        return new ResponseEntity<>(matchEventAssembler.toDTO(event), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}/match-events")
    public ResponseEntity<List<MatchEventOutput>> list(@PathVariable UUID eventId) {
        List<MatchEventOutput> outputs = matchEventService.list(eventId).stream()
                .map(matchEventAssembler::toDTO)
                .toList();
        return new ResponseEntity<>(outputs, HttpStatus.OK);
    }

    @PostMapping("/{eventId}/match-events/undo-last")
    public ResponseEntity<MatchEventOutput> undoLast(@PathVariable UUID eventId,
            @AuthenticationPrincipal Usuario usuario) {
        MatchEvent event = matchEventService.undoLast(eventId, usuario);
        return new ResponseEntity<>(matchEventAssembler.toDTO(event), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/live/state")
    public ResponseEntity<LiveStateOutput> getState(@PathVariable UUID eventId) {
        LiveState state = liveService.getState(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/start")
    public ResponseEntity<LiveStateOutput> start(@PathVariable UUID eventId) {
        LiveState state = liveService.start(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/pause")
    public ResponseEntity<LiveStateOutput> pause(@PathVariable UUID eventId) {
        LiveState state = liveService.pause(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/reset")
    public ResponseEntity<LiveStateOutput> reset(@PathVariable UUID eventId) {
        LiveState state = liveService.reset(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/live/end")
    public ResponseEntity<LiveStateOutput> end(@PathVariable UUID eventId) {
        LiveState state = liveService.end(eventId);
        return new ResponseEntity<>(liveStateAssembler.toOutput(state), HttpStatus.OK);
    }
}
