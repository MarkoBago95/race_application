package com.trail.query.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class LoggingService {

    public void logRacesQueried(int count, String userRole) {
        MDC.put("event", "RACES_QUERIED");
        MDC.put("count", String.valueOf(count));
        MDC.put("userRole", userRole);

        log.info("Races queried: {} races returned for user with role {}", count, userRole);
        MDC.clear();
    }

    public void logRaceQueried(UUID raceId, String raceName, String userRole) {
        MDC.put("event", "RACE_QUERIED");
        MDC.put("raceId", raceId.toString());
        MDC.put("raceName", raceName);
        MDC.put("userRole", userRole);

        log.info("Race queried: {} by user with role {}", raceName, userRole);
        MDC.clear();
    }

    public void logRacesSearched(String distance, int count, String userRole) {
        MDC.put("event", "RACES_SEARCHED");
        MDC.put("distance", distance);
        MDC.put("count", String.valueOf(count));
        MDC.put("userRole", userRole);

        log.info("Races searched by distance {}: {} results for user with role {}",
                distance, count, userRole);
        MDC.clear();
    }

    public void logApplicationsQueried(int count, String userRole) {
        MDC.put("event", "APPLICATIONS_QUERIED");
        MDC.put("count", String.valueOf(count));
        MDC.put("userRole", userRole);

        log.info("Applications queried: {} applications returned for user with role {}",
                count, userRole);
        MDC.clear();
    }

    public void logApplicationQueried(UUID applicationId, String applicantName, String userRole) {
        MDC.put("event", "APPLICATION_QUERIED");
        MDC.put("applicationId", applicationId.toString());
        MDC.put("applicantName", applicantName);
        MDC.put("userRole", userRole);

        log.info("Application queried: {} by user with role {}", applicantName, userRole);
        MDC.clear();
    }

    public void logApplicationsByRaceQueried(UUID raceId, int count, String userRole) {
        MDC.put("event", "APPLICATIONS_BY_RACE_QUERIED");
        MDC.put("raceId", raceId.toString());
        MDC.put("count", String.valueOf(count));
        MDC.put("userRole", userRole);

        log.info("Applications for race {} queried: {} applications returned for user with role {}",
                raceId, count, userRole);
        MDC.clear();
    }

    public void logApplicationStatsQueried(String userRole) {
        MDC.put("event", "APPLICATION_STATS_QUERIED");
        MDC.put("userRole", userRole);

        log.info("Application statistics queried by user with role {}", userRole);
        MDC.clear();
    }

    public void logEventReceived(String eventType, UUID entityId) {
        MDC.put("event", "EVENT_RECEIVED");
        MDC.put("eventType", eventType);
        MDC.put("entityId", entityId.toString());

        log.info("Event received: {} for entity {}", eventType, entityId);
        MDC.clear();
    }

    public void logEventProcessed(String eventType, UUID entityId) {
        MDC.put("event", "EVENT_PROCESSED");
        MDC.put("eventType", eventType);
        MDC.put("entityId", entityId.toString());

        log.info("Event processed successfully: {} for entity {}", eventType, entityId);
        MDC.clear();
    }

    public void logApiCall(String method, String endpoint, String userRole, long duration) {
        MDC.put("event", "API_CALL");
        MDC.put("httpMethod", method);
        MDC.put("endpoint", endpoint);
        MDC.put("userRole", userRole);
        MDC.put("duration", String.valueOf(duration));

        log.info("API call: {} {} executed in {}ms by user with role {}",
                method, endpoint, duration, userRole);
        MDC.clear();
    }

    public void logError(String operation, Exception error, UUID entityId) {
        MDC.put("event", "ERROR");
        MDC.put("operation", operation);
        if (entityId != null) {
            MDC.put("entityId", entityId.toString());
        }

        log.error("Error during {}: {}", operation, error.getMessage(), error);
        MDC.clear();
    }
}