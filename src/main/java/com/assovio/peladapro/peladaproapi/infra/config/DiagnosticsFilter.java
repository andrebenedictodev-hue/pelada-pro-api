package com.assovio.peladapro.peladaproapi.infra.config;

import com.assovio.peladapro.peladaproapi.domain.service.DiagnosticsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class DiagnosticsFilter extends OncePerRequestFilter {

    private final DiagnosticsService diagnosticsService;

    public DiagnosticsFilter(DiagnosticsService diagnosticsService) {
        this.diagnosticsService = diagnosticsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long latency = System.currentTimeMillis() - start;

        String path = request.getRequestURI();
        if (path != null && path.startsWith("/")) {
            diagnosticsService.recordRequest(request.getMethod(), path, latency);
        }
    }
}
