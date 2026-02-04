package com.ab.peladapro.peladaproapi.api.controller;

import com.ab.peladapro.peladaproapi.api.assembler.UsuarioAssembler;
import com.ab.peladapro.peladaproapi.api.dtos.request.LoginRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.request.RegisterRequestDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.AuthResponseDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.UsuarioProfileResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.service.UsuarioService;
import com.ab.peladapro.peladaproapi.infra.security.JwtService;
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
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO input) {
        Usuario usuario = usuarioService.register(input.getEmail(), input.getPassword(), input.getNickname());
        String token = jwtService.generateToken(usuario);

        AuthResponseDTO output = new AuthResponseDTO();
        output.setToken(token);
        UsuarioProfileResponseDTO profile = usuarioAssembler.toDTO(usuario);
        output.setProfile(profile);
        output.setUser(profile);

        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO input) {
        Usuario usuario = usuarioService.authenticate(input.getEmail(), input.getPassword());
        String token = jwtService.generateToken(usuario);

        AuthResponseDTO output = new AuthResponseDTO();
        output.setToken(token);
        UsuarioProfileResponseDTO profile = usuarioAssembler.toDTO(usuario);
        output.setProfile(profile);
        output.setUser(profile);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioProfileResponseDTO> me(@AuthenticationPrincipal Usuario usuario) {
        return new ResponseEntity<>(usuarioAssembler.toDTO(usuario), HttpStatus.OK);
    }
}
