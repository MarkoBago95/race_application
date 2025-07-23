package com.trail.command.service;

import com.trail.command.entity.Application;
import com.trail.command.event.ApplicationDeletedEvent;
import com.trail.command.event.RaceEventPublisher;
import com.trail.command.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final RaceEventPublisher publisher;

    public Application create(Application app) {
        app.setId(UUID.randomUUID());
        return applicationRepository.save(app);
    }

    public void delete(UUID id) {
        applicationRepository.deleteById(id);

        ApplicationDeletedEvent event = new ApplicationDeletedEvent(id);
        publisher.publishApplicationDeleted(event);
    }
}