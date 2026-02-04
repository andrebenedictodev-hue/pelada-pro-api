package com.ab.peladapro.peladaproapi.api.assembler;

import com.ab.peladapro.peladaproapi.api.dtos.response.MatchEventResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.MatchEvent;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MatchEventAssembler extends GenericAssembler<MatchEvent, MatchEventResponseDTO> {

    public MatchEventAssembler(ModelMapper strictModelMapper) {
        super(strictModelMapper, MatchEvent.class, MatchEventResponseDTO.class);
        this.strictModelMapper.typeMap(MatchEvent.class, MatchEventResponseDTO.class)
                .addMappings(mapper -> mapper.skip(MatchEventResponseDTO::setId));
    }

    @Override
    public MatchEventResponseDTO toDTO(MatchEvent entity) {
        MatchEventResponseDTO output = super.toDTO(entity);
        if (entity != null && entity.getUuid() != null) {
            output.setId(UUID.fromString(entity.getUuid()));
        }
        return output;
    }
}
