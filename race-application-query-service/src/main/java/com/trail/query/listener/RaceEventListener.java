package com.trail.query.listener;

import com.trail.query.entity.Application;
import com.trail.query.entity.Race;
import com.trail.query.entity.Race.Distance;
import com.trail.query.event.*;
import com.trail.query.repository.ApplicationRepository;
import com.trail.query.repository.RaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RaceEventListener {

    private final RaceRepository raceRepository;
    private final ApplicationRepository applicationRepository;

    @RabbitListener(queues = "application.deleted.queue")
    public void handleApplicationDeleted(ApplicationDeletedEvent event) {
        applicationRepository.deleteById(event.getId());
    }

    @RabbitListener(queues = "race.created.queue")
    public void handleRaceCreated(RaceCreatedEvent event) {
        Race race = new Race();
        race.setId(event.getId());
        race.setName(event.getName());
        race.setDistance(Distance.valueOf(event.getDistance()));
        raceRepository.save(race);
    }

    @RabbitListener(queues = "race.deleted.queue")
    public void handleRaceDeleted(RaceDeletedEvent event) {
        raceRepository.deleteById(event.getId());
    }

    @RabbitListener(queues = "race.updated.queue")
    public void handleRaceUpdated(RaceUpdatedEvent event) {
        Race race = raceRepository.findById(event.getId()).orElse(null);
        if (race != null) {
            race.setName(event.getName());
            race.setDistance(Distance.valueOf(event.getDistance()));
            raceRepository.save(race);
        }
    }

    @RabbitListener(queues = "application.created.queue")
    public void handleApplicationCreated(ApplicationCreatedEvent event) {
        Application application = new Application();
        application.setId(event.getId());
        application.setFirstName(event.getFirstName());
        application.setLastName(event.getLastName());
        application.setClub(event.getClub());
        application.setRace(event.getRace());
        applicationRepository.save(application);
    }
}