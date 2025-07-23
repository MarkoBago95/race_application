package com.trail.command.controller;

import com.trail.command.entity.Race;
import com.trail.command.service.RaceService;
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
@RequestMapping("/api/races")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Race Management", description = "API for managing trail races - Command Service")
@SecurityRequirement(name = "Bearer Authentication")
public class RaceController {

    private final RaceService raceService;
    private final LoggingService loggingService;

    @Operation(
            summary = "Create a new race",
            description = "Creates a new trail race. Only administrators can create races."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Race created successfully",
                    content = @Content(schema = @Schema(implementation = Race.class))),
            @ApiResponse(responseCode = "400", description = "Invalid race data"),
            @ApiResponse(responseCode = "403", description = "Access denied - Administrator role required"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Race> create(
            @Parameter(description = "Race data to create", required = true)
            @Valid @RequestBody Race race,
            Authentication authentication) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("Creating race: {}", race.getName());

            Race createdRace = raceService.createRace(race);

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logRaceCreated(createdRace.getId(), createdRace.getName(), userRole);
            loggingService.logApiCall("POST", "/api/races", userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Race created successfully with ID: {}", createdRace.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdRace);

        } catch (Exception e) {
            log.error("Error creating race: {}", race.getName(), e);
            loggingService.logError("CREATE_RACE", e, null);
            throw e;
        }
    }

    @Operation(
            summary = "Update an existing race",
            description = "Updates an existing trail race by ID. Only administrators can update races."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Race updated successfully",
                    content = @Content(schema = @Schema(implementation = Race.class))),
            @ApiResponse(responseCode = "404", description = "Race not found"),
            @ApiResponse(responseCode = "400", description = "Invalid race data"),
            @ApiResponse(responseCode = "403", description = "Access denied - Administrator role required")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Race> update(
            @Parameter(description = "Race ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated race data", required = true)
            @Valid @RequestBody Race race,
            Authentication authentication) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("Updating race with ID: {}", id);

            Race updatedRace = raceService.updateRace(id, race);

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logRaceUpdated(updatedRace.getId(), updatedRace.getName(), userRole);
            loggingService.logApiCall("PUT", "/api/races/" + id, userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Race updated successfully: {}", updatedRace.getName());

            return ResponseEntity.ok(updatedRace);

        } catch (Exception e) {
            log.error("Error updating race with ID: {}", id, e);
            loggingService.logError("UPDATE_RACE", e, id);
            throw e;
        }
    }

    @Operation(
            summary = "Delete a race",
            description = "Deletes an existing trail race by ID. Only administrators can delete races."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Race deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Race not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - Administrator role required")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Race ID", required = true)
            @PathVariable UUID id,
            Authentication authentication) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("Deleting race with ID: {}", id);

            raceService.deleteRace(id);

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logRaceDeleted(id, userRole);
            loggingService.logApiCall("DELETE", "/api/races/" + id, userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Race deleted successfully with ID: {}", id);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("Error deleting race with ID: {}", id, e);
            loggingService.logError("DELETE_RACE", e, id);
            throw e;
        }
    }

    private String getUserRole(Authentication authentication) {
        return authentication != null && authentication.getAuthorities() != null ?
                authentication.getAuthorities().toString() : "UNKNOWN";
    }
}