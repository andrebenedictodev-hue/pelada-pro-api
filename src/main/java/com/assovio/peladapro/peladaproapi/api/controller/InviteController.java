package com.assovio.peladapro.peladaproapi.api.controller;

import com.assovio.peladapro.peladaproapi.api.assembler.InviteAssembler;
import com.assovio.peladapro.peladaproapi.api.model.output.InviteOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Evento;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.service.EventoService;
import com.assovio.peladapro.peladaproapi.domain.service.InviteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invite")
public class InviteController {

    private final InviteService inviteService;
    private final EventoService eventoService;
    private final InviteAssembler inviteAssembler;

    public InviteController(InviteService inviteService, EventoService eventoService,
            InviteAssembler inviteAssembler) {
        this.inviteService = inviteService;
        this.eventoService = eventoService;
        this.inviteAssembler = inviteAssembler;
    }

    @GetMapping("/{inviteCode}")
    public ResponseEntity<InviteOutput> getInvite(@PathVariable String inviteCode) {
        Evento evento = inviteService.getEventoByInviteCode(inviteCode);
        long count = eventoService.countParticipants(evento.getId());
        InviteOutput output = inviteAssembler.toOutput(evento, count);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PostMapping("/{inviteCode}/accept")
    public ResponseEntity<Void> acceptInvite(@PathVariable String inviteCode,
            @AuthenticationPrincipal Usuario usuario) {
        inviteService.acceptInvite(inviteCode, usuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
