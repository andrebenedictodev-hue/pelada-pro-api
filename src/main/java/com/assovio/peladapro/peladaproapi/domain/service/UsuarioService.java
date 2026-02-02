package com.assovio.peladapro.peladaproapi.domain.service;

import com.assovio.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException;
import com.assovio.peladapro.peladaproapi.domain.exception.NaoAutorizadoException;
import com.assovio.peladapro.peladaproapi.domain.exception.NegocioException;
import com.assovio.peladapro.peladaproapi.domain.model.Friendship;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.repository.FriendshipRepository;
import com.assovio.peladapro.peladaproapi.domain.repository.PendingInviteRepository;
import com.assovio.peladapro.peladaproapi.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PendingInviteRepository pendingInviteRepository;
    private final FriendshipRepository friendshipRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            PendingInviteRepository pendingInviteRepository,
            FriendshipRepository friendshipRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.pendingInviteRepository = pendingInviteRepository;
        this.friendshipRepository = friendshipRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario register(String email, String password, String nickname) {
        String normalizedEmail = email.toLowerCase();
        if (usuarioRepository.existsByEmail(normalizedEmail)) {
            throw new NegocioException("Email already in use");
        }
        if (usuarioRepository.existsByNickname(nickname)) {
            throw new NegocioException("Nickname already in use");
        }

        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setEmail(normalizedEmail);
        usuario.setNickname(nickname);
        usuario.setSenhaHash(passwordEncoder.encode(password));
        usuario.setCreatedAt(OffsetDateTime.now());

        usuarioRepository.save(usuario);
        applyPendingInvites(usuario);

        return usuario;
    }

    public Usuario authenticate(String email, String password) {
        String normalizedEmail = email.toLowerCase();
        Usuario usuario = usuarioRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new NaoAutorizadoException("Invalid credentials"));

        if (!passwordEncoder.matches(password, usuario.getSenhaHash())) {
            throw new NaoAutorizadoException("Invalid credentials");
        }
        return usuario;
    }

    public Usuario getByIdOrThrow(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("User not found"));
    }

    public Usuario getByEmailOrNull(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario getByNicknameOrNull(String nickname) {
        return usuarioRepository.findByNickname(nickname).orElse(null);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    private void applyPendingInvites(Usuario usuario) {
        List<UUID> inviters = pendingInviteRepository.getInviters(usuario.getEmail());
        if (inviters.isEmpty()) {
            return;
        }
        for (UUID inviterId : inviters) {
            if (!friendshipRepository.existsByUserAndFriend(inviterId, usuario.getId())) {
                friendshipRepository.save(new Friendship(
                        UUID.randomUUID(), inviterId, usuario.getId(), OffsetDateTime.now()));
            }
            if (!friendshipRepository.existsByUserAndFriend(usuario.getId(), inviterId)) {
                friendshipRepository.save(new Friendship(
                        UUID.randomUUID(), usuario.getId(), inviterId, OffsetDateTime.now()));
            }
        }
        pendingInviteRepository.clearInviters(usuario.getEmail());
    }
}
