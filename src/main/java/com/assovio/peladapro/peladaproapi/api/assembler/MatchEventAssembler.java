package com.assovio.peladapro.peladaproapi.api.assembler;

import com.assovio.peladapro.peladaproapi.api.model.output.MatchEventOutput;
import com.assovio.peladapro.peladaproapi.domain.model.MatchEvent;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MatchEventAssembler extends GenericAssembler<MatchEvent, MatchEventOutput> {

    public MatchEventAssembler(ModelMapper strictModelMapper) {
        super(strictModelMapper, MatchEvent.class, MatchEventOutput.class);
    }
}
