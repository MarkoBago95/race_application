package com.trail.command.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StructuredLogger {

    // MDC Keys for structured logging
    public static final String USER_ID = "userId";
    public static final String REQUEST_ID = "requestId";
    public static final String TRACE_ID = "traceId";
    public static final String SPAN_ID = "spanId";
    public static final String ACTION = "action";
    public static final String RESOURCE_TYPE = "resourceType";
    public static final String RESOURCE_ID = "resourceId";

    private static final Logger logger = LoggerFactory.getLogger(StructuredLogger.class);

    // Business Events
    public void logRaceCreated(String userId, UUID raceId, String raceName) {
        try {
            MDC.put(USER_ID, userId);
            MDC.put(ACTION, "RACE_CREATED");
            MDC.put(RESOURCE_TYPE, "race");
            MDC.put(RESOURCE_ID, raceId.toString());

            logger.info("Race created successfully: name='{}', id='{}'", raceName, raceId);
        } finally {
            clearMDC();
        }
    }

    public void logRaceUpdated(String userId, UUID raceId, String raceName) {
        try {
            MDC.put(USER_ID, userId);
            MDC.put(ACTION, "RACE_UPDATED");
            MDC.put(RESOURCE_TYPE, "race");
            MDC.put(RESOURCE_ID, raceId.toString());

            logger.info("Race updated successfully: name='{}', id='{}'", raceName, raceId);
        } finally {
            clearMDC();
        }
    }

    public void logRaceDeleted(String userId, UUID raceId) {
        try {
            MDC.put(USER_ID, userId);
            MDC.put(ACTION, "RACE_DELETED");
            MDC.put(RESOURCE_TYPE, "race");
            MDC.put(RESOURCE_ID, raceId.toString());

            logger.info("Race deleted successfully: id='{}'", raceId);
        } finally {
            clearMDC();
        }
    }

    public void logApplicationCreated(String userId, UUID applicationId, UUID raceId) {
        try {
            MDC.put(USER_ID, userId);
            MDC.put(ACTION, "APPLICATION_CREATED");
            MDC.put(RESOURCE_TYPE, "application");
            MDC.put(RESOURCE_ID, applicationId.toString());

            logger.info("Application created successfully: id='{}', raceId='{}'", applicationId, raceId);
        } finally {
            clearMDC();
        }
    }

    // Security Events
    public void logAuthenticationAttempt(String username, boolean success, String clientIp) {
        try {
            MDC.put(ACTION, success ? "AUTH_SUCCESS" : "AUTH_FAILURE");
            MDC.put("clientIp", clientIp);

            if (success) {
                logger.info("Authentication successful for user: '{}'", username);
            } else {
                logger.warn("Authentication failed for user: '{}'", username);
            }
        } finally {
            clearMDC();
        }
    }

    public void logAuthorizationFailure(String userId, String action, String resource) {
        try {
            MDC.put(USER_ID, userId);
            MDC.put(ACTION, "AUTHORIZATION_FAILURE");
            MDC.put("attemptedAction", action);
            MDC.put("resource", resource);

            logger.warn("Authorization failed: user='{}', action='{}', resource='{}'",
                    userId, action, resource);
        } finally {
            clearMDC();
        }
    }

    // Performance Events
    public void logSlowQuery(String query, long durationMs) {
        try {
            MDC.put(ACTION, "SLOW_QUERY");
            MDC.put("queryDurationMs", String.valueOf(durationMs));

            logger.warn("Slow database query detected: duration={}ms, query='{}'",
                    durationMs, query);
        } finally {
            clearMDC();
        }
    }

    public void logApiPerformance(String endpoint, String method, long durationMs, int statusCode) {
        try {
            MDC.put(ACTION, "API_CALL");
            MDC.put("endpoint", endpoint);
            MDC.put("httpMethod", method);
            MDC.put("durationMs", String.valueOf(durationMs));
            MDC.put("statusCode", String.valueOf(statusCode));

            if (durationMs > 1000) {
                logger.warn("Slow API call: {} {} - {}ms - status:{}",
                        method, endpoint, durationMs, statusCode);
            } else {
                logger.info("API call: {} {} - {}ms - status:{}",
                        method, endpoint, durationMs, statusCode);
            }
        } finally {
            clearMDC();
        }
    }

    // Error Events
    public void logBusinessError(String userId, String action, String errorMessage, Exception e) {
        try {
            MDC.put(USER_ID, userId);
            MDC.put(ACTION, action + "_ERROR");

            logger.error("Business error occurred: action='{}', message='{}', user='{}'",
                    action, errorMessage, userId, e);
        } finally {
            clearMDC();
        }
    }

    public void logTechnicalError(String component, String operation, Exception e) {
        try {
            MDC.put(ACTION, "TECHNICAL_ERROR");
            MDC.put("component", component);
            MDC.put("operation", operation);

            logger.error("Technical error in component='{}', operation='{}': {}",
                    component, operation, e.getMessage(), e);
        } finally {
            clearMDC();
        }
    }

    // Event Processing
    public void logEventPublished(String eventType, UUID resourceId, String exchange, String routingKey) {
        try {
            MDC.put(ACTION, "EVENT_PUBLISHED");
            MDC.put("eventType", eventType);
            MDC.put(RESOURCE_ID, resourceId.toString());
            MDC.put("exchange", exchange);
            MDC.put("routingKey", routingKey);

            logger.info("Event published: type='{}', resourceId='{}', exchange='{}', routingKey='{}'",
                    eventType, resourceId, exchange, routingKey);
        } finally {
            clearMDC();
        }
    }

    public void logEventProcessingError(String eventType, String error, Exception e) {
        try {
            MDC.put(ACTION, "EVENT_PROCESSING_ERROR");
            MDC.put("eventType", eventType);

            logger.error("Event processing failed: type='{}', error='{}'", eventType, error, e);
        } finally {
            clearMDC();
        }
    }

    // Utility methods
    public void setRequestContext(String requestId, String userId) {
        MDC.put(REQUEST_ID, requestId);
        MDC.put(USER_ID, userId);
    }

    public void setTraceContext(String traceId, String spanId) {
        MDC.put(TRACE_ID, traceId);
        MDC.put(SPAN_ID, spanId);
    }

    public void clearMDC() {
        MDC.clear();
    }
}