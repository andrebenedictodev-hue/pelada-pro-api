package com.assovio.peladapro.peladaproapi.api.assembler;

import com.assovio.peladapro.peladaproapi.api.model.output.ParticipantOutput;
import com.assovio.peladapro.peladaproapi.domain.model.Participante;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ParticipanteAssembler extends GenericAssembler<Participante, ParticipantOutput> {

    public ParticipanteAssembler(ModelMapper strictModelMapper) {
        super(strictModelMapper, Participante.class, ParticipantOutput.class);
    }

    public ParticipantOutput toDTOWithDisplayName(Participante participante, String displayName) {
        ParticipantOutput output = toDTO(participante);
        output.setDisplayName(displayName);
        return output;
    }
}
