package com.assovio.peladapro.peladaproapi.api.assembler;

import com.assovio.peladapro.peladaproapi.api.model.output.LiveStateOutput;
import com.assovio.peladapro.peladaproapi.domain.model.LiveState;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LiveStateAssembler {

    private final ModelMapper strictModelMapper;

    public LiveStateAssembler(ModelMapper strictModelMapper) {
        this.strictModelMapper = strictModelMapper;
    }

    public LiveStateOutput toOutput(LiveState state) {
        return strictModelMapper.map(state, LiveStateOutput.class);
    }
}
