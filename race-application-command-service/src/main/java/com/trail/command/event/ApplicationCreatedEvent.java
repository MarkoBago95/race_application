
package com.trail.command.event;

import com.trail.command.entity.Race;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ApplicationCreatedEvent {
    private UUID id;
    private String firstName;
    private String lastName;
    private String club;
    private Race race;

    public ApplicationCreatedEvent(UUID id, String firstName, String lastName, String club, Race race) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
        this.race = race;
    }
}
