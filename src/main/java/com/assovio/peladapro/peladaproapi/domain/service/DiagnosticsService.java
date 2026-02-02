package com.assovio.peladapro.peladaproapi.domain.service;

import com.assovio.peladapro.peladaproapi.domain.model.RequestRecord;
import com.assovio.peladapro.peladaproapi.domain.repository.LiveStateRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class DiagnosticsService {

    private static final int MAX_RECORDS = 50;

    private final long startEpochMs;
    private final LinkedList<RequestRecord> records = new LinkedList<>();
    private final LiveStateRepository liveStateRepository;

    public DiagnosticsService(LiveStateRepository liveStateRepository) {
        this.liveStateRepository = liveStateRepository;
        this.startEpochMs = System.currentTimeMillis();
    }

    public void recordRequest(String method, String path, long latencyMs) {
        RequestRecord record = new RequestRecord(method, path, latencyMs, OffsetDateTime.now());
        synchronized (records) {
            records.addFirst(record);
            while (records.size() > MAX_RECORDS) {
                records.removeLast();
            }
        }
    }

    public long uptimeMs() {
        return System.currentTimeMillis() - startEpochMs;
    }

    public long serverEpochMs() {
        return System.currentTimeMillis();
    }

    public long estimatedLatencyMs() {
        synchronized (records) {
            if (records.isEmpty()) {
                return 0;
            }
            long sum = 0;
            for (RequestRecord record : records) {
                sum += record.getLatencyMs();
            }
            return sum / records.size();
        }
    }

    public List<RequestRecord> lastRequests() {
        synchronized (records) {
            return List.copyOf(records);
        }
    }

    public int subscriptions() {
        return liveStateRepository.count();
    }
}
