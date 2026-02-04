package com.ab.peladapro.peladaproapi.api.controller;

import com.ab.peladapro.peladaproapi.api.assembler.FriendshipAssembler;
import com.ab.peladapro.peladaproapi.api.dtos.request.FriendInviteRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.FriendResponseDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.UsuarioSearchResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.service.FriendshipService;
import com.ab.peladapro.peladaproapi.domain.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UsuarioService usuarioService;
    private final FriendshipAssembler friendshipAssembler;

    @GetMapping("/friends")
    public ResponseEntity<List<FriendResponseDTO>> listFriends(@AuthenticationPrincipal Usuario usuario) {
        List<FriendResponseDTO> outputs = friendshipService.listFriends(usuario).stream()
                .map(friendshipAssembler::toFriendOutput)
                .toList();
        return new ResponseEntity<>(outputs, HttpStatus.OK);
    }

    @PostMapping("/friends/invite")
    public ResponseEntity<Void> inviteFriend(@AuthenticationPrincipal Usuario usuario,
            @RequestBody @Valid FriendInviteRequestDTO input) {
        friendshipService.inviteByEmail(usuario, input.getInviteeEmail());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/search")
    public ResponseEntity<UsuarioSearchResponseDTO> searchByEmail(@RequestParam("email") String email) {
        Usuario usuario = usuarioService.getByEmailOrNull(email);
        if (usuario == null) {
            throw new com.ab.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException(
                    "User not found");
        }
        UsuarioSearchResponseDTO output = new UsuarioSearchResponseDTO();
        output.setId(UUID.fromString(usuario.getUuid()));
        output.setNickname(usuario.getNickname());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
