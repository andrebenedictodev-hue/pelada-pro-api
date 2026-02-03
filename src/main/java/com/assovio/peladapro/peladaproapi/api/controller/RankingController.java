package com.assovio.peladapro.peladaproapi.api.controller;

import com.assovio.peladapro.peladaproapi.api.assembler.RankingAssembler;
import com.assovio.peladapro.peladaproapi.api.model.output.RankingOutput;
import com.assovio.peladapro.peladaproapi.domain.service.RankingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class RankingController {

    private final RankingService rankingService;
    private final RankingAssembler rankingAssembler;

    public RankingController(RankingService rankingService, RankingAssembler rankingAssembler) {
        this.rankingService = rankingService;
        this.rankingAssembler = rankingAssembler;
    }

    @GetMapping("/ranking")
    public ResponseEntity<RankingOutput> global() {
        return new ResponseEntity<>(rankingAssembler.toOutput(rankingService.getGlobal()), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/ranking")
    public ResponseEntity<RankingOutput> byEvent(@PathVariable UUID eventId) {
        return new ResponseEntity<>(rankingAssembler.toOutput(rankingService.getByEvent(eventId)), HttpStatus.OK);
    }
}
