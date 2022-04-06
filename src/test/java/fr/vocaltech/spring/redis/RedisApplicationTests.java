package fr.vocaltech.spring.redis;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

import fr.vocaltech.spring.redis.models.Position;
import fr.vocaltech.spring.redis.repositories.PositionRepository;

@SpringBootTest
public class RedisApplicationTests {
	@Autowired
	PositionRepository positionRepository;

	@Test
	public void whenSavingPosition_thenAvailableOnRetrieval() {
		Position position = new Position(45.5, 1.5, Instant.now().toEpochMilli(), "track_id", "userId");

		Position savedPos = positionRepository.save(position);
		String id = savedPos.getId();

		Optional<Position> pos = positionRepository.findById(id);

		pos.ifPresent(v -> assertEquals(45.5, v.getLatitude()));
		pos.ifPresent(v -> assertEquals(1.5, v.getLongitude()));

		pos.ifPresent(v -> {
			Instant tsInstant = Instant.ofEpochMilli(v.getTime());
			String isoTs = DateTimeFormatter.ISO_INSTANT.format(tsInstant);
			System.out.println("ISO ts: " + isoTs);
		});
	}
}
