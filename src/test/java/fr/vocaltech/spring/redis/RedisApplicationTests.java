package fr.vocaltech.spring.redis;

import fr.vocaltech.spring.redis.models.Position;
import fr.vocaltech.spring.redis.repositories.PositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
public class RedisApplicationTests {
	@Autowired
	PositionRepository positionRepository;

	@Test
	public void whenSavingPosition_thenAvailableOnRetrieval() {
		Position position = new Position(45.5, 1.5, Instant.now().toEpochMilli(), "track_id", "userId");
		positionRepository.save(position);

		positionRepository.findAll().forEach(System.out::println);
	}
}
