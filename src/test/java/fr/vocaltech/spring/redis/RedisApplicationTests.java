package fr.vocaltech.spring.redis;

import lombok.var;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

import fr.vocaltech.spring.redis.models.Position;
import fr.vocaltech.spring.redis.repositories.PositionRepository;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisApplicationTests {
	@Autowired
	PositionRepository positionRepository;

	@Autowired
	RedisOperations<String, String> operations;

	private GeoOperations<String, String> geoOperations;

	@Test
	@Order(1)
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

	@Test
	@Order(2)
	public void testGeoAdd() {
		// geoadd cities 1.4848071122378244 43.54025760156568 ramonville

		geoOperations = operations.opsForGeo();

		geoOperations.add("cities", new Point(1.4848071122378244, 43.54025760156568), "Ramonville");

	}

	@Test
	@Order(3)
	public void testGeoRadius() {
		// georadius cities 1.4881504856522094 43.532946878330776 2 km

		var circle = new Circle(new Point(1.4881504856522094, 43.532946878330776),
				new Distance(2, RedisGeoCommands.DistanceUnit.KILOMETERS));

		var result = geoOperations.radius("cities", circle);

		System.out.println("radius res: " + result);

	}
}
