package com.assovio.peladapro.peladaproapi.domain.service;

import com.assovio.peladapro.peladaproapi.domain.model.Friendship;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.repository.FriendshipRepository;
import com.assovio.peladapro.peladaproapi.domain.repository.PendingInviteRepository;
import com.assovio.peladapro.peladaproapi.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final PendingInviteRepository pendingInviteRepository;
    private final UsuarioRepository usuarioRepository;

    public FriendshipService(FriendshipRepository friendshipRepository,
            PendingInviteRepository pendingInviteRepository,
            UsuarioRepository usuarioRepository) {
        this.friendshipRepository = friendshipRepository;
        this.pendingInviteRepository = pendingInviteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listFriends(Usuario usuario) {
        return friendshipRepository.findByUserId(usuario.getId()).stream()
                .map(friendship -> usuarioRepository.findById(friendship.getFriendUserId()).orElse(null))
                .filter(item -> item != null)
                .toList();
    }

    public void inviteByEmail(Usuario inviter, String inviteeEmail) {
        Usuario invitee = usuarioRepository.findByEmail(inviteeEmail).orElse(null);
        if (invitee != null) {
            createFriendshipPair(inviter.getId(), invitee.getId());
        } else {
            pendingInviteRepository.addInvite(inviteeEmail, inviter.getId());
        }
    }

    public void createFriendshipPair(UUID a, UUID b) {
        if (!friendshipRepository.existsByUserAndFriend(a, b)) {
            friendshipRepository.save(new Friendship(UUID.randomUUID(), a, b, OffsetDateTime.now()));
        }
        if (!friendshipRepository.existsByUserAndFriend(b, a)) {
            friendshipRepository.save(new Friendship(UUID.randomUUID(), b, a, OffsetDateTime.now()));
        }
    }
}
