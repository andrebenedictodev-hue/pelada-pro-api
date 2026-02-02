package com.assovio.peladapro.peladaproapi.api.assembler;

import com.assovio.peladapro.peladaproapi.api.model.output.EventOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Evento;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EventoAssembler extends GenericAssembler<Evento, EventOutput> {

    public EventoAssembler(ModelMapper strictModelMapper) {
        super(strictModelMapper, Evento.class, EventOutput.class);
    }

    public EventOutput toDTOWithCount(Evento evento, long participantsCount) {
        EventOutput output = toDTO(evento);
        output.setParticipantsCount(participantsCount);
        return output;
    }
}
