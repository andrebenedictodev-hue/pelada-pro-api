package com.ab.peladapro.peladaproapi.api.controller;

import com.ab.peladapro.peladaproapi.api.assembler.InviteAssembler;
import com.ab.peladapro.peladaproapi.api.dtos.response.InviteResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Evento;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.service.EventoService;
import com.ab.peladapro.peladaproapi.domain.service.InviteService;

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

import java.util.UUID;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/invite")
public class InviteController {

    private final InviteService inviteService;
    private final EventoService eventoService;
    private final InviteAssembler inviteAssembler;

    @GetMapping("/{inviteCode}")
    public ResponseEntity<InviteResponseDTO> getInvite(@PathVariable String inviteCode) {
        Evento evento = inviteService.getEventoByInviteCode(inviteCode);
        long count = eventoService.countParticipants(UUID.fromString(evento.getUuid()));
        InviteResponseDTO output = inviteAssembler.toOutput(evento, count);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PostMapping("/{inviteCode}/accept")
    public ResponseEntity<Void> acceptInvite(@PathVariable String inviteCode,
            @AuthenticationPrincipal Usuario usuario) {
        inviteService.acceptInvite(inviteCode, usuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
