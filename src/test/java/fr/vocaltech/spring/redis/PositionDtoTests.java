package fr.vocaltech.spring.redis;

import fr.vocaltech.spring.redis.dto.PositionDto;
import fr.vocaltech.spring.redis.models.Position;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.modelmapper.ModelMapper;

import java.time.Instant;

public class PositionDtoTests {
    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    void whenConvertPositionDtoToModel_thenCorrect() {
        PositionDto positionDto = new PositionDto();

        long now = Instant.now().toEpochMilli();

        positionDto.setId("position_id");
        positionDto.setLatitude(45.2);
        positionDto.setLongitude(1.5);
        positionDto.setTime(now);
        positionDto.setTrackId("track_1");
        positionDto.setUserId("user_1");

        Position position = modelMapper.map(positionDto, Position.class);

        assertThat(positionDto.getId()).isEqualTo(position.getId());
        assertThat(positionDto.getLatitude()).isEqualTo(position.getLatitude());
        assertThat(positionDto.getLongitude()).isEqualTo(position.getLongitude());
        assertThat(positionDto.getTime()).isEqualTo(position.getTime());
        assertThat(positionDto.getTrackId()).isEqualTo(position.getTrackId());
        assertThat(positionDto.getUserId()).isEqualTo(position.getUserId());
    }

    @Test
    void whenConvertPositionModelToDto_thenCorrect() {
        Position position = new Position(43.2, 1.7, Instant.now().toEpochMilli(), "track_2", "user_2");
        position.setId("position_id");

        PositionDto positionDto = modelMapper.map(position, PositionDto.class);

        assertThat(position.getId()).isEqualTo(positionDto.getId());
        assertThat(position.getLatitude()).isEqualTo(positionDto.getLatitude());
        assertThat(position.getLongitude()).isEqualTo(positionDto.getLongitude());
    }
}
