package com.assovio.peladapro.peladaproapi.api.controller;

import com.assovio.peladapro.peladaproapi.api.assembler.EventoAssembler;
import com.assovio.peladapro.peladaproapi.api.model.input.EventCreateInput;
import com.assovio.peladapro.peladaproapi.api.model.output.EventListOutput;
import com.assovio.peladapro.peladaproapi.api.model.output.EventOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Evento;
import com.assovio.peladapro.peladaproapi.domain.model.EventoStatus;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.service.EventoService;
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
public class EventoController {

    private final EventoService eventoService;
    private final EventoAssembler eventoAssembler;

    @PostMapping
    public ResponseEntity<EventOutput> create(@AuthenticationPrincipal Usuario usuario,
            @RequestBody @Valid EventCreateInput input) {
        Evento evento = eventoService.create(input, usuario);
        long count = eventoService.countParticipants(evento.getId());
        EventOutput output = eventoAssembler.toDTOWithCount(evento, count);
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<EventListOutput> list() {
        List<Evento> eventos = eventoService.listAll();

        List<EventOutput> upcoming = eventos.stream()
                .filter(evento -> evento.getStatus() == EventoStatus.UPCOMING)
                .map(evento -> eventoAssembler.toDTOWithCount(evento, eventoService.countParticipants(evento.getId())))
                .toList();

        List<EventOutput> live = eventos.stream()
                .filter(evento -> evento.getStatus() == EventoStatus.LIVE)
                .map(evento -> eventoAssembler.toDTOWithCount(evento, eventoService.countParticipants(evento.getId())))
                .toList();

        List<EventOutput> finished = eventos.stream()
                .filter(evento -> evento.getStatus() == EventoStatus.FINISHED)
                .map(evento -> eventoAssembler.toDTOWithCount(evento, eventoService.countParticipants(evento.getId())))
                .toList();

        EventListOutput output = new EventListOutput();
        output.setUpcoming(upcoming);
        output.setLive(live);
        output.setFinished(finished);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventOutput> getById(@PathVariable UUID eventId) {
        Evento evento = eventoService.getById(eventId);
        long count = eventoService.countParticipants(eventId);
        EventOutput output = eventoAssembler.toDTOWithCount(evento, count);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
