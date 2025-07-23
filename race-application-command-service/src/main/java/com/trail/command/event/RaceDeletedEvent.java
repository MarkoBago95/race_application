package com.trail.command.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class RaceDeletedEvent {
    private UUID id;

    public RaceDeletedEvent(UUID id) {
        this.id = id;
    }
}
