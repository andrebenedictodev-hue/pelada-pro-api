package com.ab.peladapro.peladaproapi.domain.model;

import com.ab.peladapro.peladaproapi.domain.model.contracts.EntityBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "live_state")
@SQLRestriction("deleted_at IS NULL")
public class LiveState extends EntityBase {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<List<UUID>>> TEAMS_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<UUID>> UUID_LIST_TYPE = new TypeReference<>() {};

    @Column(name = "event_uuid", nullable = false, unique = true)
    private String eventId;

    @Column(name = "started_at_server_epoch_ms", nullable = false)
    private long startedAtServerEpochMs;

    @Column(name = "accumulated_time_ms", nullable = false)
    private long accumulatedTimeMs;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LiveStatus status;

    @Column(name = "score_a", nullable = false)
    private int scoreA;

    @Column(name = "score_b", nullable = false)
    private int scoreB;

    @Column(name = "teams_json")
    private String teamsJson;

    @Column(name = "queue_json")
    private String queueJson;

    @Column(name = "leaders_json")
    private String leadersJson;

    @Transient
    private List<List<UUID>> teams;

    @Transient
    private List<UUID> queue;

    @Transient
    private List<UUID> leaders;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public long getStartedAtServerEpochMs() {
        return startedAtServerEpochMs;
    }

    public void setStartedAtServerEpochMs(long startedAtServerEpochMs) {
        this.startedAtServerEpochMs = startedAtServerEpochMs;
    }

    public long getAccumulatedTimeMs() {
        return accumulatedTimeMs;
    }

    public void setAccumulatedTimeMs(long accumulatedTimeMs) {
        this.accumulatedTimeMs = accumulatedTimeMs;
    }

    public LiveStatus getStatus() {
        return status;
    }

    public void setStatus(LiveStatus status) {
        this.status = status;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    public List<List<UUID>> getTeams() {
        if (teams == null) {
            teams = parseTeams(teamsJson);
        }
        return teams;
    }

    public void setTeams(List<List<UUID>> teams) {
        this.teams = teams != null ? teams : new ArrayList<>();
        this.teamsJson = toJson(this.teams);
    }

    public List<UUID> getQueue() {
        if (queue == null) {
            queue = parseUuidList(queueJson);
        }
        return queue;
    }

    public void setQueue(List<UUID> queue) {
        this.queue = queue != null ? queue : new ArrayList<>();
        this.queueJson = toJson(this.queue);
    }

    public List<UUID> getLeaders() {
        if (leaders == null) {
            leaders = parseUuidList(leadersJson);
        }
        return leaders;
    }

    public void setLeaders(List<UUID> leaders) {
        this.leaders = leaders != null ? leaders : new ArrayList<>();
        this.leadersJson = toJson(this.leaders);
    }

    private static List<List<UUID>> parseTeams(String json) {
        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(json, TEAMS_TYPE);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static List<UUID> parseUuidList(String json) {
        if (json == null || json.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(json, UUID_LIST_TYPE);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static String toJson(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            return "[]";
        }
    }
}
