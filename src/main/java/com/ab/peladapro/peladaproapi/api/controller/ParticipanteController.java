package com.ab.peladapro.peladaproapi.api.controller;

import com.ab.peladapro.peladaproapi.api.assembler.ParticipanteAssembler;
import com.ab.peladapro.peladaproapi.api.dtos.response.ParticipanteResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Participante;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.service.EventoService;
import com.ab.peladapro.peladaproapi.domain.service.UsuarioService;

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
    public ResponseEntity<ParticipanteResponseDTO> join(@PathVariable UUID eventId,
            @AuthenticationPrincipal Usuario usuario) {
        Participante participante = eventoService.join(eventId, usuario);
        String displayName = usuarioService.getByIdOrThrow(UUID.fromString(usuario.getUuid())).getNickname();
        ParticipanteResponseDTO output = participanteAssembler.toDTOWithDisplayName(participante, displayName);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PostMapping("/{eventId}/leave")
    public ResponseEntity<Void> leave(@PathVariable UUID eventId, @AuthenticationPrincipal Usuario usuario) {
        eventoService.leave(eventId, usuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<ParticipanteResponseDTO>> participants(@PathVariable UUID eventId) {
        List<Participante> participantes = eventoService.listParticipants(eventId);
        List<ParticipanteResponseDTO> outputs = participantes.stream()
                .map(participante -> {
                    String name = participante.getGuestName();
                    if (name == null && participante.getUserId() != null) {
                        name = usuarioService.getByIdOrThrow(UUID.fromString(participante.getUserId())).getNickname();
                    }
                    return participanteAssembler.toDTOWithDisplayName(participante, name);
                })
                .toList();
        return new ResponseEntity<>(outputs, HttpStatus.OK);
    }

    @PostMapping("/{eventId}/participants/{userId}/remove")
    public ResponseEntity<Void> removeParticipant(@PathVariable UUID eventId, @PathVariable UUID userId,
            @AuthenticationPrincipal Usuario usuario) {
        eventoService.removeParticipant(eventId, userId, usuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
