package com.assovio.peladapro.peladaproapi.domain.service;

import com.assovio.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException;
import com.assovio.peladapro.peladaproapi.domain.model.Evento;
import com.assovio.peladapro.peladaproapi.domain.model.Invite;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.repository.InviteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InviteService {

    private final InviteRepository inviteRepository;
    private final EventoService eventoService;
    private final FriendshipService friendshipService;

    public InviteService(InviteRepository inviteRepository, EventoService eventoService,
            FriendshipService friendshipService) {
        this.inviteRepository = inviteRepository;
        this.eventoService = eventoService;
        this.friendshipService = friendshipService;
    }

    public Evento getEventoByInviteCode(String inviteCode) {
        Invite invite = inviteRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Invite not found"));
        return eventoService.getById(invite.getEventId());
    }

    public void acceptInvite(String inviteCode, Usuario usuario) {
        Evento evento = getEventoByInviteCode(inviteCode);
        eventoService.join(evento.getId(), usuario);
        UUID organizerId = evento.getOwnerId();
        friendshipService.createFriendshipPair(organizerId, usuario.getId());
    }
}
