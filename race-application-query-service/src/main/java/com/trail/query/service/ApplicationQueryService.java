package com.trail.query.service;

import com.trail.query.entity.Application;
import com.trail.query.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationQueryService {
    private final ApplicationRepository applicationRepository;

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Application findOne(UUID id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public List<Application> findByRaceId(UUID raceId) {
        return applicationRepository.findByRaceId(raceId);
    }
}