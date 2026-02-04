package com.ab.peladapro.peladaproapi.api.assembler;

import com.ab.peladapro.peladaproapi.api.dtos.response.UsuarioProfileResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioAssembler extends GenericAssembler<Usuario, UsuarioProfileResponseDTO> {

    public UsuarioAssembler(ModelMapper strictModelMapper) {
        super(strictModelMapper, Usuario.class, UsuarioProfileResponseDTO.class);
        this.strictModelMapper.typeMap(Usuario.class, UsuarioProfileResponseDTO.class)
                .addMappings(mapper -> mapper.skip(UsuarioProfileResponseDTO::setId));
    }

    @Override
    public UsuarioProfileResponseDTO toDTO(Usuario entity) {
        UsuarioProfileResponseDTO output = super.toDTO(entity);
        if (entity != null && entity.getUuid() != null) {
            output.setId(UUID.fromString(entity.getUuid()));
        }
        return output;
    }
}
