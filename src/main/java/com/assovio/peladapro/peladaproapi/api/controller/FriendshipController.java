package com.assovio.peladapro.peladaproapi.api.controller;

import com.assovio.peladapro.peladaproapi.api.assembler.FriendshipAssembler;
import com.assovio.peladapro.peladaproapi.api.model.input.FriendInviteInput;
import com.assovio.peladapro.peladaproapi.api.model.output.FriendOutput;
import com.assovio.peladapro.peladaproapi.api.model.output.UserSearchOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.service.FriendshipService;
import com.assovio.peladapro.peladaproapi.domain.service.UsuarioService;
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

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UsuarioService usuarioService;
    private final FriendshipAssembler friendshipAssembler;

    @GetMapping("/friends")
    public ResponseEntity<List<FriendOutput>> listFriends(@AuthenticationPrincipal Usuario usuario) {
        List<FriendOutput> outputs = friendshipService.listFriends(usuario).stream()
                .map(friendshipAssembler::toFriendOutput)
                .toList();
        return new ResponseEntity<>(outputs, HttpStatus.OK);
    }

    @PostMapping("/friends/invite")
    public ResponseEntity<Void> inviteFriend(@AuthenticationPrincipal Usuario usuario,
            @RequestBody @Valid FriendInviteInput input) {
        friendshipService.inviteByEmail(usuario, input.getInviteeEmail());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/search")
    public ResponseEntity<UserSearchOutput> searchByEmail(@RequestParam("email") String email) {
        Usuario usuario = usuarioService.getByEmailOrNull(email);
        if (usuario == null) {
            throw new com.assovio.peladapro.peladaproapi.domain.exception.EntidadeNaoEncontradaException(
                    "User not found");
        }
        UserSearchOutput output = new UserSearchOutput();
        output.setId(usuario.getId());
        output.setNickname(usuario.getNickname());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
