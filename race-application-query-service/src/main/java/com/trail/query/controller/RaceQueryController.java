package com.trail.query.controller;

import com.trail.query.entity.Race;
import com.trail.query.service.RaceQueryService;
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
@RequestMapping("/api/races")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Race Query", description = "API for querying trail races - Query Service")
@SecurityRequirement(name = "Bearer Authentication")
public class RaceQueryController {

    private final RaceQueryService raceQueryService;
    private final LoggingService loggingService;

    @Operation(
            summary = "Get all races",
            description = "Retrieves all available trail races. Both applicants and administrators can view races."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Races retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Race.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasRole( 'ADMINISTRATOR')")
    public ResponseEntity<List<Race>> getAllRaces(Authentication authentication) {
        long startTime = System.currentTimeMillis();

        try {
            log.info("Fetching all races");

            List<Race> races = raceQueryService.findAll();

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logRacesQueried(races.size(), userRole);
            loggingService.logApiCall("GET", "/api/races", userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Successfully retrieved {} races", races.size());

            return ResponseEntity.ok(races);

        } catch (Exception e) {
            log.error("Error fetching all races", e);
            loggingService.logError("GET_ALL_RACES", e, null);
            throw e;
        }
    }

    @Operation(
            summary = "Get race by ID",
            description = "Retrieves a specific trail race by its ID. Both applicants and administrators can view race details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Race retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Race.class))),
            @ApiResponse(responseCode = "404", description = "Race not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT token required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole( 'ADMINISTRATOR')")
    public ResponseEntity<Race> getRaceById(
            @Parameter(description = "Race ID", required = true)
            @PathVariable UUID id,
            Authentication authentication) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("Fetching race with ID: {}", id);

            Race race = raceQueryService.findOne(id);

            // Structured logging
            String userRole = getUserRole(authentication);
            loggingService.logRaceQueried(race.getId(), race.getName(), userRole);
            loggingService.logApiCall("GET", "/api/races/" + id, userRole,
                    System.currentTimeMillis() - startTime);

            log.info("Successfully retrieved race: {}", race.getName());

            return ResponseEntity.ok(race);

        } catch (Exception e) {
            log.error("Error fetching race with ID: {}", id, e);
            loggingService.logError("GET_RACE_BY_ID", e, id);
            throw e;
        }
    }

    private String getUserRole(Authentication authentication) {
        return authentication != null && authentication.getAuthorities() != null ?
                authentication.getAuthorities().toString() : "UNKNOWN";
    }
}