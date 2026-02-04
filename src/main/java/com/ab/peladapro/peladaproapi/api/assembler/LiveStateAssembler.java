package com.ab.peladapro.peladaproapi.api.assembler;

import com.ab.peladapro.peladaproapi.api.dtos.response.LiveStateResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.LiveState;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LiveStateAssembler {

    private final ModelMapper strictModelMapper;

    public LiveStateAssembler(ModelMapper strictModelMapper) {
        this.strictModelMapper = strictModelMapper;
    }

    public LiveStateResponseDTO toOutput(LiveState state) {
        return strictModelMapper.map(state, LiveStateResponseDTO.class);
    }
}
