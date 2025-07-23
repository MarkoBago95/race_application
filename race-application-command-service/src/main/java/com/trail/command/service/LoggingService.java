package com.trail.command.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class LoggingService {

    public void logRaceCreated(UUID raceId, String raceName, String userRole) {
        MDC.put("event", "RACE_CREATED");
        MDC.put("raceId", raceId.toString());
        MDC.put("raceName", raceName);
        MDC.put("userRole", userRole);

        log.info("Race created successfully: {} by user with role {}", raceName, userRole);
        MDC.clear();
    }

    public void logRaceUpdated(UUID raceId, String raceName, String userRole) {
        MDC.put("event", "RACE_UPDATED");
        MDC.put("raceId", raceId.toString());
        MDC.put("raceName", raceName);
        MDC.put("userRole", userRole);

        log.info("Race updated successfully: {} by user with role {}", raceName, userRole);
        MDC.clear();
    }

    public void logRaceDeleted(UUID raceId, String userRole) {
        MDC.put("event", "RACE_DELETED");
        MDC.put("raceId", raceId.toString());
        MDC.put("userRole", userRole);

        log.info("Race deleted: {} by user with role {}", raceId, userRole);
        MDC.clear();
    }

    public void logEventPublished(String eventType, UUID entityId) {
        MDC.put("event", "EVENT_PUBLISHED");
        MDC.put("eventType", eventType);
        MDC.put("entityId", entityId.toString());

        log.info("Event published: {} for entity {}", eventType, entityId);
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
    public void logApplicationCreated(UUID applicationId, String applicantName, UUID raceId, String userRole) {
        MDC.put("event", "APPLICATION_CREATED");
        MDC.put("applicationId", applicationId.toString());
        MDC.put("applicantName", applicantName);
        MDC.put("raceId", raceId.toString());
        MDC.put("userRole", userRole);

        log.info("Application created successfully: {} for race {} by user with role {}",
                applicantName, raceId, userRole);
        MDC.clear();
    }

    public void logApplicationDeleted(UUID applicationId, String userRole) {
        MDC.put("event", "APPLICATION_DELETED");
        MDC.put("applicationId", applicationId.toString());
        MDC.put("userRole", userRole);

        log.info("Application deleted: {} by user with role {}", applicationId, userRole);
        MDC.clear();
    }

    public void logApplicationAccessDenied(UUID applicationId, String userRole, String reason) {
        MDC.put("event", "APPLICATION_ACCESS_DENIED");
        MDC.put("applicationId", applicationId.toString());
        MDC.put("userRole", userRole);
        MDC.put("reason", reason);

        log.warn("Access denied to application {}: {} for user with role {}",
                applicationId, reason, userRole);
        MDC.clear();
    }
}