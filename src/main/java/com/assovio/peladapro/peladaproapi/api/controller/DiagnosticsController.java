package com.assovio.peladapro.peladaproapi.api.controller;

import com.assovio.peladapro.peladaproapi.api.model.output.DiagnosticsOutput;
import com.assovio.peladapro.peladaproapi.api.model.output.RequestRecordOutput;
import com.assovio.peladapro.peladaproapi.domain.exception.NaoAutorizadoException;
import com.assovio.peladapro.peladaproapi.domain.model.RequestRecord;
import com.assovio.peladapro.peladaproapi.domain.model.Usuario;
import com.assovio.peladapro.peladaproapi.domain.service.DiagnosticsService;
import com.assovio.peladapro.peladaproapi.domain.service.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/diagnostics")
public class DiagnosticsController {

    private final DiagnosticsService diagnosticsService;
    private final EventoService eventoService;

    public DiagnosticsController(DiagnosticsService diagnosticsService, EventoService eventoService) {
        this.diagnosticsService = diagnosticsService;
        this.eventoService = eventoService;
    }

    @GetMapping
    public ResponseEntity<DiagnosticsOutput> diagnostics(@AuthenticationPrincipal Usuario usuario) {
        if (!eventoService.hasOwnerEvents(usuario.getId())) {
            throw new NaoAutorizadoException("Organizer only");
        }

        DiagnosticsOutput output = new DiagnosticsOutput();
        output.setUptimeMs(diagnosticsService.uptimeMs());
        output.setServerEpochMs(diagnosticsService.serverEpochMs());
        output.setEstimatedLatencyMs(diagnosticsService.estimatedLatencyMs());
        output.setOnline(true);
        output.setSubscriptions(diagnosticsService.subscriptions());

        List<RequestRecordOutput> records = diagnosticsService.lastRequests().stream()
                .map(this::toOutput)
                .toList();
        output.setLastRequests(records);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    private RequestRecordOutput toOutput(RequestRecord record) {
        RequestRecordOutput output = new RequestRecordOutput();
        output.setMethod(record.getMethod());
        output.setPath(record.getPath());
        output.setLatencyMs(record.getLatencyMs());
        output.setTimestamp(record.getTimestamp());
        return output;
    }
}
