package com.assovio.peladapro.peladaproapi.api.controller;

import com.assovio.peladapro.peladaproapi.api.assembler.ParticipanteAssembler;
import com.assovio.peladapro.peladaproapi.api.model.output.ParticipantOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Participante;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.service.EventoService;
import com.assovio.peladapro.peladaproapi.domain.service.UsuarioService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/events")
public class ParticipanteController {

    private final EventoService eventoService;
    private final ParticipanteAssembler participanteAssembler;
    private final UsuarioService usuarioService;

    @PostMapping("/{eventId}/join")
    public ResponseEntity<ParticipantOutput> join(@PathVariable UUID eventId,
            @AuthenticationPrincipal Usuario usuario) {
        Participante participante = eventoService.join(eventId, usuario);
        String displayName = usuarioService.getByIdOrThrow(usuario.getId()).getNickname();
        ParticipantOutput output = participanteAssembler.toDTOWithDisplayName(participante, displayName);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PostMapping("/{eventId}/leave")
    public ResponseEntity<Void> leave(@PathVariable UUID eventId, @AuthenticationPrincipal Usuario usuario) {
        eventoService.leave(eventId, usuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<ParticipantOutput>> participants(@PathVariable UUID eventId) {
        List<Participante> participantes = eventoService.listParticipants(eventId);
        List<ParticipantOutput> outputs = participantes.stream()
                .map(participante -> {
                    String name = participante.getGuestName();
                    if (name == null && participante.getUserId() != null) {
                        name = usuarioService.getByIdOrThrow(participante.getUserId()).getNickname();
                    }
                    return participanteAssembler.toDTOWithDisplayName(participante, name);
                })
                .toList();
        return new ResponseEntity<>(outputs, HttpStatus.OK);
    }
}
