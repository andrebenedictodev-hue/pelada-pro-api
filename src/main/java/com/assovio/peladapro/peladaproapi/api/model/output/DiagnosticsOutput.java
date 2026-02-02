package com.assovio.peladapro.peladaproapi.api.model.output;

import java.util.List;

public class DiagnosticsOutput {

    private long uptimeMs;
    private long serverEpochMs;
    private long estimatedLatencyMs;
    private boolean online;
    private int subscriptions;
    private List<RequestRecordOutput> lastRequests;

    public long getUptimeMs() {
        return uptimeMs;
    }

    public void setUptimeMs(long uptimeMs) {
        this.uptimeMs = uptimeMs;
    }

    public long getServerEpochMs() {
        return serverEpochMs;
    }

    public void setServerEpochMs(long serverEpochMs) {
        this.serverEpochMs = serverEpochMs;
    }

    public long getEstimatedLatencyMs() {
        return estimatedLatencyMs;
    }

    public void setEstimatedLatencyMs(long estimatedLatencyMs) {
        this.estimatedLatencyMs = estimatedLatencyMs;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(int subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<RequestRecordOutput> getLastRequests() {
        return lastRequests;
    }

    public void setLastRequests(List<RequestRecordOutput> lastRequests) {
        this.lastRequests = lastRequests;
    }
}
