package com.ab.peladapro.peladaproapi.domain.service;

import com.ab.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException;
import com.ab.peladapro.peladaproapi.domain.exception.NaoAutorizadoException;
import com.ab.peladapro.peladaproapi.domain.exception.NegocioException;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.dao.UsuarioDAO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioDAO usuarioDAO,
            PasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario register(String email, String password, String nickname) {
        String normalizedEmail = email.toLowerCase();
        if (usuarioDAO.existsByEmail(normalizedEmail)) {
            throw new NegocioException("Email already in use");
        }
        if (usuarioDAO.existsByNickname(nickname)) {
            throw new NegocioException("Nickname already in use");
        }

        Usuario usuario = new Usuario();
        usuario.setUuid(UUID.randomUUID().toString());
        usuario.setEmail(normalizedEmail);
        usuario.setNickname(nickname);
        usuario.setSenhaHash(passwordEncoder.encode(password));

        usuarioDAO.save(usuario);
        return usuario;
    }

    public Usuario authenticate(String email, String password) {
        String normalizedEmail = email.toLowerCase();
        Usuario usuario = usuarioDAO.findFirstByEmail(normalizedEmail)
                .orElseThrow(() -> new NaoAutorizadoException("Invalid credentials"));

        if (!passwordEncoder.matches(password, usuario.getSenhaHash())) {
            throw new NaoAutorizadoException("Invalid credentials");
        }
        return usuario;
    }

    public Usuario getByIdOrThrow(UUID id) {
        return usuarioDAO.findFirstByUuid(id.toString())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("User not found"));
    }

    public Usuario getByEmailOrNull(String email) {
        return usuarioDAO.findFirstByEmail(email).orElse(null);
    }

    public Usuario getByNicknameOrNull(String nickname) {
        return usuarioDAO.findFirstByNickname(nickname).orElse(null);
    }

    public List<Usuario> findAll() {
        return usuarioDAO.findAll();
    }

}
