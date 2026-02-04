package com.ab.peladapro.peladaproapi.api.assembler;

import com.ab.peladapro.peladaproapi.api.dtos.response.RankingEntryResponseDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.RankingResponseDTO;
import com.ab.peladapro.peladaproapi.domain.model.RankingEntry;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RankingAssembler {

    private final ModelMapper strictModelMapper;

    public RankingAssembler(ModelMapper strictModelMapper) {
        this.strictModelMapper = strictModelMapper;
    }

    public RankingResponseDTO toOutput(List<RankingEntry> entries) {
        RankingResponseDTO output = new RankingResponseDTO();
        List<RankingEntryResponseDTO> mapped = entries.stream()
                .map(entry -> strictModelMapper.map(entry, RankingEntryResponseDTO.class))
                .toList();
        output.setEntries(mapped);
        return output;
    }
}
