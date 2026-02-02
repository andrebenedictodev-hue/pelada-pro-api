package com.assovio.peladapro.peladaproapi.api.assembler;

import com.assovio.peladapro.peladaproapi.api.model.output.UserProfileOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAssembler extends GenericAssembler<Usuario, UserProfileOutput> {

    public UsuarioAssembler(ModelMapper strictModelMapper) {
        super(strictModelMapper, Usuario.class, UserProfileOutput.class);
    }
}
