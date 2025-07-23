package com.trail.command.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ApplicationDeletedEvent {
    private UUID id;

    public ApplicationDeletedEvent(UUID id) {
        this.id = id;
    }
}