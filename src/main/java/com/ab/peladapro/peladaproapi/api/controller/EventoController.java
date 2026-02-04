package com.ab.peladapro.peladaproapi.api.controller;

import com.ab.peladapro.peladaproapi.api.assembler.EventoAssembler;
import com.ab.peladapro.peladaproapi.api.dtos.request.EventoRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.EventoListResponseDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.EventoResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Evento;
import com.ab.peladapro.peladaproapi.domain.model.EventoStatus;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.service.EventoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class EventoController {

    private final EventoService eventoService;
    private final EventoAssembler eventoAssembler;

    @PostMapping
    public ResponseEntity<EventoResponseDTO> create(@AuthenticationPrincipal Usuario usuario,
            @RequestBody @Valid EventoRequestDTO input) {
        Evento evento = eventoService.create(input, usuario);
        long count = eventoService.countParticipants(UUID.fromString(evento.getUuid()));
        EventoResponseDTO output = eventoAssembler.toDTOWithCount(evento, count);
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<EventoListResponseDTO> list() {
        List<Evento> eventos = eventoService.listAll();

        List<EventoResponseDTO> upcoming = eventos.stream()
                .filter(evento -> evento.getStatus() == EventoStatus.UPCOMING)
                .map(evento -> eventoAssembler.toDTOWithCount(evento, eventoService.countParticipants(UUID.fromString(evento.getUuid()))))
                .toList();

        List<EventoResponseDTO> live = eventos.stream()
                .filter(evento -> evento.getStatus() == EventoStatus.LIVE)
                .map(evento -> eventoAssembler.toDTOWithCount(evento, eventoService.countParticipants(UUID.fromString(evento.getUuid()))))
                .toList();

        List<EventoResponseDTO> finished = eventos.stream()
                .filter(evento -> evento.getStatus() == EventoStatus.FINISHED)
                .map(evento -> eventoAssembler.toDTOWithCount(evento, eventoService.countParticipants(UUID.fromString(evento.getUuid()))))
                .toList();

        EventoListResponseDTO output = new EventoListResponseDTO();
        output.setUpcoming(upcoming);
        output.setLive(live);
        output.setFinished(finished);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventoResponseDTO> getById(@PathVariable UUID eventId) {
        Evento evento = eventoService.getById(eventId);
        long count = eventoService.countParticipants(eventId);
        EventoResponseDTO output = eventoAssembler.toDTOWithCount(evento, count);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable UUID eventId, @AuthenticationPrincipal Usuario usuario) {
        eventoService.deleteEvent(eventId, usuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
