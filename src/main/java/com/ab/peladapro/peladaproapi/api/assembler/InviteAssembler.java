package com.ab.peladapro.peladaproapi.api.assembler;

import com.ab.peladapro.peladaproapi.api.dtos.response.InviteResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.Evento;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InviteAssembler {

    public InviteResponseDTO toOutput(Evento evento, long participantsCount) {
        InviteResponseDTO output = new InviteResponseDTO();
        output.setEventId(UUID.fromString(evento.getUuid()));
        output.setTitle(evento.getTitle());
        output.setLocation(evento.getLocation());
        output.setType(evento.getType());
        output.setStatus(evento.getStatus());
        output.setParticipantsCount(participantsCount);
        return output;
    }
}
