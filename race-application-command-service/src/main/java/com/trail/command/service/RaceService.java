package com.trail.command.service;

import com.trail.command.entity.Application;
import com.trail.command.entity.Race;
import com.trail.command.event.ApplicationCreatedEvent;
import com.trail.command.event.RaceCreatedEvent;
import com.trail.command.event.RaceEventPublisher;
import com.trail.command.event.RaceUpdatedEvent;
import com.trail.command.repository.ApplicationRepository;
import com.trail.command.repository.RaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RaceService {
    private final RaceRepository raceRepository;
    private final RaceEventPublisher publisher;
    private final ApplicationRepository applicationRepository;

    public Race createRace(Race race) {
        race.setId(UUID.randomUUID());
        Race saved = raceRepository.save(race);

        // Pošalji event
        RaceCreatedEvent event = new RaceCreatedEvent(
                saved.getId(),
                saved.getName(),
                saved.getDistance()
        );
        publisher.publishRaceCreated(event);

        return saved;
    }

    public Application create(Application app) {
        app.setId(UUID.randomUUID());
        Application saved = applicationRepository.save(app);

        ApplicationCreatedEvent event = new ApplicationCreatedEvent(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getClub(),
                saved.getRace()
        );
        publisher.publishApplicationCreated(event);

        return saved;
    }


    public Race updateRace(UUID id, Race updated) {
        Race existing = raceRepository.findById(id).orElseThrow();
        existing.setName(updated.getName());
        existing.setDistance(updated.getDistance());
        Race saved = raceRepository.save(existing);

        RaceUpdatedEvent event = new RaceUpdatedEvent(
                saved.getId(),
                saved.getName(),
                saved.getDistance()
        );
        publisher.publishRaceUpdated(event);

        return saved;
    }

    public void deleteRace(UUID id) {
        raceRepository.deleteById(id);
    }

}