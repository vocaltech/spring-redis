package fr.vocaltech.spring.redis;

import lombok.var;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;

import fr.vocaltech.spring.redis.models.Position;
import fr.vocaltech.spring.redis.repositories.PositionRepository;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedisApplicationTests {
	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private RedisOperations<String, String> operations;

	private GeoOperations<String, String> geoOperations;

	@BeforeEach
	void init() {
		geoOperations = operations.opsForGeo();
	}

	@Test
	@Tag("persistence")
	@Order(1)
	void whenSavingPosition_thenAvailableOnRetrieval() {
		Position position = new Position(45.5, 1.5, Instant.now().toEpochMilli(), "track_id", "userId");

		Position savedPos = positionRepository.save(position);
		String id = savedPos.getId();

		Optional<Position> pos = positionRepository.findById(id);

		pos.ifPresent(v -> {
			Instant tsInstant = Instant.ofEpochMilli(v.getTime());
			String isoTs = DateTimeFormatter.ISO_INSTANT.format(tsInstant);
			System.out.println("ISO ts: " + isoTs);

			assertThat(v.getLatitude()).isEqualTo(45.5);
			assertThat(v.getLongitude()).isEqualTo(1.5);
		});
	}

	@Test
	@Tag("geo")
	@Order(1)
	void testGeoAdd() {
		geoOperations.add("gym31", new Point(1.481123, 43.538924), "Basic Fit:31520:Ramonville");
		geoOperations.add("gym31", new Point(1.495027, 43.524487), "Movida:31320:Castanet-Tolosan");

		var positions = geoOperations.position("gym31", "Basic Fit:31520:Ramonville", "Movida:31320:Castanet-Tolosan");
		assertThat(positions.size()).isEqualTo(2);
	}

	@Test
	@Tag("geo")
	@Order(2)
	void testGeoDist() {
		var distance = geoOperations.distance("gym31",
				"Basic Fit:31520:Ramonville",
				"Movida:31320:Castanet-Tolosan",
				RedisGeoCommands.DistanceUnit.KILOMETERS);

		assertThat(distance.getValue()).isBetween(1.5, 2.0);
	}

	@Test
	@Tag("geo")
	@Order(3)
	void testGeoRadius() {
		var circle = new Circle(new Point(1.4874561145698026, 43.53105707122094),
				new Distance(1.1, RedisGeoCommands.DistanceUnit.KILOMETERS));

		var radiusResult = geoOperations.radius("gym31", circle);
		var results = radiusResult.getContent();

		assertThat(results.size()).isEqualTo(2);
		assertThat(results.get(0).getContent().getName()).startsWith("Basic Fit");
		assertThat(results.get(1).getContent().getName()).startsWith("Movida");
	}

	@Test
	@Tag("geo")
	@Order(4)
	void testGeoRadiusByMember_whenDistanceLower_2kms_thenReturn_NoMembers() {
		var geoResults = geoOperations.radius("gym31",
				"Basic Fit:31520:Ramonville",
				new Distance(1.9, RedisGeoCommands.DistanceUnit.KILOMETERS));

		assertThat(geoResults.getContent().size()).isEqualTo(1);
	}

	@Test
	@Tag("geo")
	@Order(5)
	void testGeoRadiusByMember_whenDistanceEquals_2kms_thenReturn_1Member() {
		var geoResults = geoOperations.radius("gym31",
				"Basic Fit:31520:Ramonville",
				new Distance(2.0, RedisGeoCommands.DistanceUnit.KILOMETERS));

		assertThat(geoResults.getContent().size()).isEqualTo(2);
	}
}
