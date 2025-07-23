package com.trail.query.service;

import com.trail.query.entity.Race;
import com.trail.query.repository.RaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RaceQueryServiceTest {

    @Mock
    private RaceRepository raceRepository;

    @InjectMocks
    private RaceQueryService raceQueryService;

    @Test
    void shouldReturnAllRaces() {
        // Given
        Race race1 = createRace("Zagreb Marathon", Race.Distance.Marathon);
        Race race2 = createRace("Plitvice Trail", Race.Distance.HalfMarathon);

        when(raceRepository.findAll()).thenReturn(Arrays.asList(race1, race2));

        // When
        List<Race> result = raceQueryService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Zagreb Marathon");
        assertThat(result.get(1).getName()).isEqualTo("Plitvice Trail");
    }

    @Test
    void shouldReturnRaceById() {
        // Given
        UUID raceId = UUID.randomUUID();
        Race race = createRace("Test Race", Race.Distance.TenK);
        race.setId(raceId);

        when(raceRepository.findById(raceId)).thenReturn(Optional.of(race));

        // When
        Race result = raceQueryService.findOne(raceId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(raceId);
        assertThat(result.getName()).isEqualTo("Test Race");
    }

    @Test
    void shouldThrowExceptionWhenRaceNotFound() {
        // Given
        UUID raceId = UUID.randomUUID();
        when(raceRepository.findById(raceId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> raceQueryService.findOne(raceId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Race not found");
    }

    @Test
    void shouldReturnEmptyListWhenNoRaces() {
        // Given
        when(raceRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Race> result = raceQueryService.findAll();

        // Then
        assertThat(result).isEmpty();
    }

    private Race createRace(String name, Race.Distance distance) {
        Race race = new Race();
        race.setId(UUID.randomUUID());
        race.setName(name);
        race.setDistance(distance);
        return race;
    }
}