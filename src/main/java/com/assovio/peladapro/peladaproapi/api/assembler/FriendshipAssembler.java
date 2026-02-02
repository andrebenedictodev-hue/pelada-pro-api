package com.assovio.peladapro.peladaproapi.api.assembler;

import com.assovio.peladapro.peladaproapi.api.model.output.FriendOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class FriendshipAssembler {

    public FriendOutput toFriendOutput(Usuario usuario) {
        FriendOutput output = new FriendOutput();
        output.setUserId(usuario.getId());
        output.setNickname(usuario.getNickname());
        output.setEmail(usuario.getEmail());
        return output;
    }
}
