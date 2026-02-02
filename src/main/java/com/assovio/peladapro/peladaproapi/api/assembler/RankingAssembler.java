package com.assovio.peladapro.peladaproapi.api.assembler;

import com.assovio.peladapro.peladaproapi.api.model.output.RankingEntryOutput;
import com.assovio.peladapro.peladaproapi.api.model.output.RankingOutput;
import com.assovio.peladapro.peladaproapi.domain.model.RankingEntry;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RankingAssembler {

    private final ModelMapper strictModelMapper;

    public RankingAssembler(ModelMapper strictModelMapper) {
        this.strictModelMapper = strictModelMapper;
    }

    public RankingOutput toOutput(List<RankingEntry> entries) {
        RankingOutput output = new RankingOutput();
        List<RankingEntryOutput> mapped = entries.stream()
                .map(entry -> strictModelMapper.map(entry, RankingEntryOutput.class))
                .toList();
        output.setEntries(mapped);
        return output;
    }
}
