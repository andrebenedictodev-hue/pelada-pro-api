package com.ab.peladapro.peladaproapi.api.assembler;

import com.ab.peladapro.peladaproapi.api.dtos.response.EventoResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Evento;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventoAssembler extends GenericAssembler<Evento, EventoResponseDTO> {

    public EventoAssembler(ModelMapper strictModelMapper) {
        super(strictModelMapper, Evento.class, EventoResponseDTO.class);
        this.strictModelMapper.typeMap(Evento.class, EventoResponseDTO.class)
                .addMappings(mapper -> mapper.skip(EventoResponseDTO::setId));
    }

    public EventoResponseDTO toDTOWithCount(Evento evento, long participantsCount) {
        EventoResponseDTO output = toDTO(evento);
        output.setParticipantsCount(participantsCount);
        return output;
    }

    @Override
    public EventoResponseDTO toDTO(Evento entity) {
        EventoResponseDTO output = super.toDTO(entity);
        if (entity != null) {
            if (entity.getUuid() != null) {
                output.setId(UUID.fromString(entity.getUuid()));
            }
            if (entity.getOwnerId() != null) {
                output.setOwnerId(UUID.fromString(entity.getOwnerId()));
            }
        }
        return output;
    }
}
