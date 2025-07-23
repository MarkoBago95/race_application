package com.trail.command.controller;

import com.trail.command.entity.Application;
import com.trail.command.service.ApplicationService;
import com.trail.command.service.LoggingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Application Management", description = "API for managing race applications - Command Service")
@SecurityRequirement(name = "Bearer Authentication")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final LoggingService loggingService;

    @Operation(
            summary = "Create a new application",
            description = "Creates a new race application. Both applicants and administrators can create applications."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Application created successfully",
                    content = @Content(schema = @Schema(implementation = Application.class))),
            @ApiResponse(responseCode = "400", description = "Invalid application data"),
            @ApiResponse(responseCode = "404", description = "Race not found"),
            @ApiResponse(responseCode = "409", description = "Application already exists for this race"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'APPLICANT')")
    public ResponseEntity<Application> create(
            @Parameter(description = "Application data to create", required = true)
            @Valid @RequestBody Application app,
            Authentication authentication) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("Creating application for race ID: {} by user: {}",
                    app.getRace().getId(), app.getFirstName() + " " + app.getLastName());

            Application createdApp = applicationService.create(app);

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logApplicationCreated(createdApp.getId(),
                    createdApp.getFirstName() + " " + createdApp.getLastName(),
                    createdApp.getRace().getId(), userRole);
            loggingService.logApiCall("POST", "/api/applications", userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Application created successfully with ID: {} for race: {}",
                    createdApp.getId(), createdApp.getRace());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdApp);

        } catch (Exception e) {
            log.error("Error creating application for race ID: {}", app.getRace().getId(), e);
            loggingService.logError("CREATE_APPLICATION", e, null);
            throw e;
        }
    }

    @Operation(
            summary = "Delete an application",
            description = "Deletes an existing race application by ID. Applicants can delete their own applications, administrators can delete any application."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Application deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Application not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - Cannot delete other user's application"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'APPLICANT')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Application ID", required = true)
            @PathVariable UUID id,
            Authentication authentication) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("Deleting application with ID: {}", id);

            // TODO: Add logic to check if applicant can only delete their own applications
            // For now, allowing all authenticated users as per current PreAuthorize

            applicationService.delete(id);

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logApplicationDeleted(id, userRole);
            loggingService.logApiCall("DELETE", "/api/applications/" + id, userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Application deleted successfully with ID: {}", id);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("Error deleting application with ID: {}", id, e);
            loggingService.logError("DELETE_APPLICATION", e, id);
            throw e;
        }
    }

    private String getUserRole(Authentication authentication) {
        return authentication != null && authentication.getAuthorities() != null ?
                authentication.getAuthorities().toString() : "UNKNOWN";
    }
}