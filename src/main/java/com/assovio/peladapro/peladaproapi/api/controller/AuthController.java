package com.assovio.peladapro.peladaproapi.api.controller;

import com.assovio.peladapro.peladaproapi.api.assembler.UsuarioAssembler;
import com.assovio.peladapro.peladaproapi.api.model.input.LoginInput;
import com.assovio.peladapro.peladaproapi.api.model.input.RegisterInput;
import com.assovio.peladapro.peladaproapi.api.model.output.AuthOutput;
import com.assovio.peladapro.peladaproapi.api.model.output.UserProfileOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.service.UsuarioService;
import com.assovio.peladapro.peladaproapi.infra.security.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final UsuarioAssembler usuarioAssembler;

    @PostMapping("/register")
    public ResponseEntity<AuthOutput> register(@RequestBody @Valid RegisterInput input) {
        Usuario usuario = usuarioService.register(input.getEmail(), input.getPassword(), input.getNickname());
        String token = jwtService.generateToken(usuario);

        AuthOutput output = new AuthOutput();
        output.setToken(token);
        UserProfileOutput profile = usuarioAssembler.toDTO(usuario);
        output.setProfile(profile);
        output.setUser(profile);

        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthOutput> login(@RequestBody @Valid LoginInput input) {
        Usuario usuario = usuarioService.authenticate(input.getEmail(), input.getPassword());
        String token = jwtService.generateToken(usuario);

        AuthOutput output = new AuthOutput();
        output.setToken(token);
        UserProfileOutput profile = usuarioAssembler.toDTO(usuario);
        output.setProfile(profile);
        output.setUser(profile);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileOutput> me(@AuthenticationPrincipal Usuario usuario) {
        return new ResponseEntity<>(usuarioAssembler.toDTO(usuario), HttpStatus.OK);
    }
}
