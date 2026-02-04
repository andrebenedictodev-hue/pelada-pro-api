package com.ab.peladapro.peladaproapi.api.controller;

import com.ab.peladapro.peladaproapi.api.dtos.response.DiagnosticsResponseDTO;
import com.ab.peladapro.peladaproapi.api.dtos.response.RequestRecordResponseDTO;
import com.ab.peladapro.peladaproapi.domain.exception.NaoAutorizadoException;
import com.ab.peladapro.peladaproapi.domain.model.RequestRecord;
import com.ab.peladapro.peladaproapi.domain.model.Usuario;
import com.ab.peladapro.peladaproapi.domain.service.DiagnosticsService;
import com.ab.peladapro.peladaproapi.domain.service.EventoService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/diagnostics")
public class DiagnosticsController {

    private final DiagnosticsService diagnosticsService;
    private final EventoService eventoService;

    @GetMapping
    public ResponseEntity<DiagnosticsResponseDTO> diagnostics(@AuthenticationPrincipal Usuario usuario) {
        if (!eventoService.hasOwnerEvents(UUID.fromString(usuario.getUuid()))) {
            throw new NaoAutorizadoException("Organizer only");
        }

        DiagnosticsResponseDTO output = new DiagnosticsResponseDTO();
        output.setUptimeMs(diagnosticsService.uptimeMs());
        output.setServerEpochMs(diagnosticsService.serverEpochMs());
        output.setEstimatedLatencyMs(diagnosticsService.estimatedLatencyMs());
        output.setOnline(true);
        output.setSubscriptions(diagnosticsService.subscriptions());

        List<RequestRecordResponseDTO> records = diagnosticsService.lastRequests().stream()
                .map(this::toOutput)
                .toList();
        output.setLastRequests(records);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    private RequestRecordResponseDTO toOutput(RequestRecord record) {
        RequestRecordResponseDTO output = new RequestRecordResponseDTO();
        output.setMethod(record.getMethod());
        output.setPath(record.getPath());
        output.setLatencyMs(record.getLatencyMs());
        output.setTimestamp(record.getTimestamp());
        return output;
    }
}
