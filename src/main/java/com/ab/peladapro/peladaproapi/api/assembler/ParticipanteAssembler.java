package com.ab.peladapro.peladaproapi.api.assembler;

import com.ab.peladapro.peladaproapi.api.dtos.response.ParticipanteResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Participante;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ParticipanteAssembler extends GenericAssembler<Participante, ParticipanteResponseDTO> {

    public ParticipanteAssembler(ModelMapper strictModelMapper) {
        super(strictModelMapper, Participante.class, ParticipanteResponseDTO.class);
        this.strictModelMapper.typeMap(Participante.class, ParticipanteResponseDTO.class)
                .addMappings(mapper -> mapper.skip(ParticipanteResponseDTO::setId));
    }

    public ParticipanteResponseDTO toDTOWithDisplayName(Participante participante, String displayName) {
        ParticipanteResponseDTO output = toDTO(participante);
        output.setDisplayName(displayName);
        return output;
    }

    @Override
    public ParticipanteResponseDTO toDTO(Participante entity) {
        ParticipanteResponseDTO output = super.toDTO(entity);
        if (entity != null && entity.getUuid() != null) {
            output.setId(UUID.fromString(entity.getUuid()));
        }
        return output;
    }
}
