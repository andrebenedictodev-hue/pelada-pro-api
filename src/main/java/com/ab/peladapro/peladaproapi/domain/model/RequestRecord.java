package com.ab.peladapro.peladaproapi.domain.model;

import java.time.OffsetDateTime;

public class RequestRecord {

    private String method;
    private String path;
    private long latencyMs;
    private OffsetDateTime timestamp;

    public RequestRecord() {
    }

    public RequestRecord(String method, String path, long latencyMs, OffsetDateTime timestamp) {
        this.method = method;
        this.path = path;
        this.latencyMs = latencyMs;
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(long latencyMs) {
        this.latencyMs = latencyMs;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
