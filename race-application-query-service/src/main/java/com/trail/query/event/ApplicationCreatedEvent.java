package com.trail.query.event;

import com.trail.query.entity.Race;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCreatedEvent {
    private UUID id;
    private String firstName;
    private String lastName;
    private String club;
    private Race race;
}