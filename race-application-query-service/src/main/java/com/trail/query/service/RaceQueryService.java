package com.trail.query.service;

import com.trail.query.entity.Race;
import com.trail.query.repository.RaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RaceQueryService {
    private final RaceRepository raceRepository;

    public List<Race> findAll() {
        return raceRepository.findAll();
    }

    public Race findOne(UUID id) {
        return raceRepository.findById(id).orElse(null);
    }
}
