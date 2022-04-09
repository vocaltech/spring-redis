package fr.vocaltech.spring.redis;

import lombok.var;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

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
	private PositionRepository positionRepository;

	@Autowired
	private RedisOperations<String, String> operations;

	private GeoOperations<String, String> geoOperations;

	@BeforeEach
	void beforeEach() {
		geoOperations = operations.opsForGeo();
	}

	@Disabled
	@Test
	@Order(1)
	public void whenSavingPosition_thenAvailableOnRetrieval() {
		Position position = new Position(45.5, 1.5, Instant.now().toEpochMilli(), "track_id", "userId");

		Position savedPos = positionRepository.save(position);
		String id = savedPos.getId();

		Optional<Position> pos = positionRepository.findById(id);

		/*
		pos.ifPresent(v -> assertEquals(45.5, v.getLatitude()));
		pos.ifPresent(v -> assertEquals(1.5, v.getLongitude()));
		 */

		pos.ifPresent(v -> {
			Instant tsInstant = Instant.ofEpochMilli(v.getTime());
			String isoTs = DateTimeFormatter.ISO_INSTANT.format(tsInstant);
			System.out.println("ISO ts: " + isoTs);
		});
	}

	@Test
	@Order(2)
	public void testGeoAdd() {
		geoOperations.add("gym31", new Point(1.4811232701076127, 43.53892440303041), "Basic Fit:31520:Ramonville");
		geoOperations.add("gym31", new Point(1.49502784119392, 43.52448781411589), "Movida:31320:Castanet-Tolosan");

		var positions = geoOperations.position("gym31", "Basic Fit:31520:Ramonville", "Movida:31320:Castanet-Tolosan");
		assertThat(positions.size()).isEqualTo(2);
	}

	@Test
	@Order(3)
	public void testGeoDist() {
		var distance = geoOperations.distance("gym31",
				"Basic Fit:31520:Ramonville",
				"Movida:31320:Castanet-Tolosan",
				RedisGeoCommands.DistanceUnit.KILOMETERS);

		assertThat(distance.getValue()).isBetween(1.5, 2.0);
	}

	@Test
	@Order(4)
	public void testGeoRadius() {
		var circle = new Circle(new Point(1.4874561145698026, 43.53105707122094),
				new Distance(1.1, RedisGeoCommands.DistanceUnit.KILOMETERS));

		var radiusResult = geoOperations.radius("gym31", circle);
		var results = radiusResult.getContent();

		assertThat(results.size()).isEqualTo(2);
		assertThat(results.get(0).getContent().getName()).startsWith("Basic Fit");
		assertThat(results.get(1).getContent().getName()).startsWith("Movida");
	}
}
