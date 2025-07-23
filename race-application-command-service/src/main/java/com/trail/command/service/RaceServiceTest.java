package com.trail.command.service;

// ← IMPORTS
import com.trail.command.entity.Race;
import com.trail.command.entity.Race.Distance;
import com.trail.command.event.RaceCreatedEvent;
import com.trail.command.repository.RaceRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RaceServiceTest {

    @Mock
    private RaceRepository raceRepository;

    @InjectMocks
    private RaceService raceService;

    @Test
    void shouldCreateRaceSuccessfully() {
        // Given
        RaceCreatedEvent request = new RaceCreatedEvent(UUID.randomUUID(),"Zagreb Marathon", Distance.Marathon);
        Race requestPrepresentations=new Race(request.getId(),request.getName(),request.getDistance());
        Race savedRace = new Race();
        savedRace.setId(UUID.randomUUID());
        savedRace.setName("Zagreb Marathon");
        savedRace.setDistance(Distance.Marathon);

        when(raceRepository.save(any(Race.class))).thenReturn(savedRace);

        // When
        Race result = raceService.createRace(requestPrepresentations);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Zagreb Marathon");
        assertThat(result.getDistance()).isEqualTo(Distance.Marathon);
        verify(raceRepository).save(any(Race.class));
    }

    @Test
    void shouldUpdateRaceSuccessfully() {
        // Given
        UUID raceId = UUID.randomUUID();
        Race existingRace = new Race();
        existingRace.setId(raceId);
        existingRace.setName("Old Name");
        existingRace.setDistance(Distance.FiveK);

        Race updatedRace = new Race();
        updatedRace.setId(raceId);
        updatedRace.setName("New Name");
        updatedRace.setDistance(Distance.Marathon);

        when(raceRepository.findById(raceId)).thenReturn(Optional.of(existingRace));
        when(raceRepository.save(any(Race.class))).thenReturn(updatedRace);

        // When
        Race result = raceService.updateRace(raceId, updatedRace);

        // Then
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getDistance()).isEqualTo(Distance.Marathon);
        verify(raceRepository).findById(raceId);
        verify(raceRepository).save(existingRace);
    }

    @Test
    void shouldThrowExceptionWhenRaceNotFound() {
        // Given
        UUID raceId = UUID.randomUUID();
        when(raceRepository.findById(raceId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> raceService.updateRace(raceId, new Race()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Race not found");
    }

    @Test
    void shouldDeleteRaceSuccessfully() {
        // Given
        UUID raceId = UUID.randomUUID();
        Race existingRace = new Race();
        existingRace.setId(raceId);

        when(raceRepository.findById(raceId)).thenReturn(Optional.of(existingRace));

        // When
        raceService.deleteRace(raceId);

        // Then
        verify(raceRepository).findById(raceId);
        verify(raceRepository).delete(existingRace);
    }
}