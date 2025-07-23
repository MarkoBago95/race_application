package com.trail.query.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceCreatedEvent {
    private UUID id;
    private String name;
    private String distance;
}

