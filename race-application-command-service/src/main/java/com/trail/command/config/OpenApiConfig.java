package com.trail.command.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI trailRaceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Trail Race Command Service API")
                        .description("""
                            Trail Race Management System - Command Service
                            
                            This service handles all write operations (CREATE, UPDATE, DELETE) for the trail race management system.
                            It follows CQRS architecture and publishes events to RabbitMQ for the Query Service to consume.
                            
                            ## Authentication
                            All endpoints require JWT Bearer token authentication.
                            
                            ## Authorization
                            - **ADMINISTRATOR**: Can manage races and applications
                            - **APPLICANT**: Can create and manage their own applications
                            
                            ## Events
                            This service publishes the following events:
                            - `RaceCreatedEvent` - When a new race is created
                            - `RaceUpdatedEvent` - When a race is updated
                            - `RaceDeletedEvent` - When a race is deleted
                            - `ApplicationDeletedEvent` - When an application is deleted
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Trail Race Team")
                                .email("support@trailrace.com")
                                .url("https://trailrace.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development server"),
                        new Server()
                                .url("https://api.trailrace.com")
                                .description("Production server")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Bearer token authentication")));
    }
}