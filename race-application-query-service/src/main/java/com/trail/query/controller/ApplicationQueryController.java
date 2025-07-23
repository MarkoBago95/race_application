package com.trail.query.controller;

import com.trail.query.entity.Application;
import com.trail.query.service.ApplicationQueryService;
import com.trail.query.service.LoggingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Application Query", description = "API for querying race applications - Query Service")
@SecurityRequirement(name = "Bearer Authentication")
public class ApplicationQueryController {

    private final ApplicationQueryService applicationService;
    private final LoggingService loggingService;

    @Operation(
            summary = "Get all applications",
            description = "Retrieves all race applications. Administrators can see all applications, applicants can see their own applications."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Application.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('APPLICANT', 'ADMINISTRATOR')")
    public ResponseEntity<List<Application>> getAll(Authentication authentication) {
        long startTime = System.currentTimeMillis();

        try {
            log.info("Fetching all applications");

            List<Application> applications = applicationService.findAll();

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logApplicationsQueried(applications.size(), userRole);
            loggingService.logApiCall("GET", "/api/applications", userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Successfully retrieved {} applications", applications.size());

            return ResponseEntity.ok(applications);

        } catch (Exception e) {
            log.error("Error fetching all applications", e);
            loggingService.logError("GET_ALL_APPLICATIONS", e, null);
            throw e;
        }
    }

    @Operation(
            summary = "Get application by ID",
            description = "Retrieves a specific race application by its ID. Administrators can view any application, applicants can view their own applications."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Application.class))),
            @ApiResponse(responseCode = "404", description = "Application not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - Cannot view other user's application"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT', 'ADMINISTRATOR')")
    public ResponseEntity<Application> getOne(
            @Parameter(description = "Application ID", required = true)
            @PathVariable UUID id,
            Authentication authentication) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("Fetching application with ID: {}", id);

            Application application = applicationService.findOne(id);

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logApplicationQueried(application.getId(),
                    application.getFirstName() + " " + application.getLastName(),
                    userRole);
            loggingService.logApiCall("GET", "/api/applications/" + id, userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Successfully retrieved application for: {} {}",
                    application.getFirstName(), application.getLastName());

            return ResponseEntity.ok(application);

        } catch (Exception e) {
            log.error("Error fetching application with ID: {}", id, e);
            loggingService.logError("GET_APPLICATION_BY_ID", e, id);
            throw e;
        }
    }

    @Operation(
            summary = "Get applications by race ID",
            description = "Retrieves all applications for a specific race. Administrators can view all race applications, applicants can view applications for races they applied to."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Applications retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Application.class))),
            @ApiResponse(responseCode = "404", description = "Race not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required")
    })
    @GetMapping("/race/{raceId}")
    @PreAuthorize("hasAnyRole('APPLICANT', 'ADMINISTRATOR')")
    public ResponseEntity<List<Application>> getByRace(
            @Parameter(description = "Race ID", required = true)
            @PathVariable UUID raceId,
            Authentication authentication) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("Fetching applications for race ID: {}", raceId);

            List<Application> applications = applicationService.findByRaceId(raceId);

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logApplicationsByRaceQueried(raceId, applications.size(), userRole);
            loggingService.logApiCall("GET", "/api/applications/race/" + raceId, userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Found {} applications for race ID: {}", applications.size(), raceId);

            return ResponseEntity.ok(applications);

        } catch (Exception e) {
            log.error("Error fetching applications for race ID: {}", raceId, e);
            loggingService.logError("GET_APPLICATIONS_BY_RACE", e, raceId);
            throw e;
        }
    }

    private String getUserRole(Authentication authentication) {
        return authentication != null && authentication.getAuthorities() != null ?
                authentication.getAuthorities().toString() : "UNKNOWN";
    }
}