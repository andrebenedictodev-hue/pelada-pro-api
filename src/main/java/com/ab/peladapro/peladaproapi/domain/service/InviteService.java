package com.ab.peladapro.peladaproapi.domain.service;

import com.ab.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException;
import com.ab.peladapro.peladaproapi.domain.model.Evento;
import com.ab.peladapro.peladaproapi.domain.model.Invite;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.dao.InviteDAO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InviteService {

    private final InviteDAO inviteDAO;
    private final EventoService eventoService;
    private final FriendshipService friendshipService;

    public InviteService(InviteDAO inviteDAO, EventoService eventoService,
            FriendshipService friendshipService) {
        this.inviteDAO = inviteDAO;
        this.eventoService = eventoService;
        this.friendshipService = friendshipService;
    }

    public Evento getEventoByInviteCode(String inviteCode) {
        Invite invite = inviteDAO.findFirstByInviteCode(inviteCode)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Invite not found"));
        return eventoService.getById(UUID.fromString(invite.getEventId()));
    }

    public void acceptInvite(String inviteCode, Usuario usuario) {
        Evento evento = getEventoByInviteCode(inviteCode);
        eventoService.join(UUID.fromString(evento.getUuid()), usuario);
        String organizerId = evento.getOwnerId();
        friendshipService.createFriendshipPair(organizerId, usuario.getUuid());
    }
}
