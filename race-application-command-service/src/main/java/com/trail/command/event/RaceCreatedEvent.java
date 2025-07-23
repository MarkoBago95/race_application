package com.trail.command.event;

import com.trail.command.entity.Race;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor

public class RaceCreatedEvent {
    private UUID id;
    private String name;
    private Race.Distance distance;

    public RaceCreatedEvent(UUID id, String name, Race.Distance distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
    }
}