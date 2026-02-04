package com.ab.peladapro.peladaproapi.api.assembler;

import com.ab.peladapro.peladaproapi.api.dtos.response.FriendResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FriendshipAssembler {

    public FriendResponseDTO toFriendOutput(Usuario usuario) {
        FriendResponseDTO output = new FriendResponseDTO();
        output.setUserId(UUID.fromString(usuario.getUuid()));
        output.setNickname(usuario.getNickname());
        output.setEmail(usuario.getEmail());
        return output;
    }
}
