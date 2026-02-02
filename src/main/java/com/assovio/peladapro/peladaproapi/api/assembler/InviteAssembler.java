package com.assovio.peladapro.peladaproapi.api.assembler;

import com.assovio.peladapro.peladaproapi.api.model.output.InviteOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Evento;
import org.springframework.stereotype.Component;

@Component
public class InviteAssembler {

    public InviteOutput toOutput(Evento evento, long participantsCount) {
        InviteOutput output = new InviteOutput();
        output.setEventId(evento.getId());
        output.setTitle(evento.getTitle());
        output.setLocation(evento.getLocation());
        output.setType(evento.getType());
        output.setStatus(evento.getStatus());
        output.setParticipantsCount(participantsCount);
        return output;
    }
}
