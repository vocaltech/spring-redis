package fr.vocaltech.spring.redis;

import lombok.var;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import java.util.Locale;
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
		Position position = new Position(45.5, 1.5, Instant.now().toEpochMilli(), "track_1", "user_1");
		Position savedPos = positionRepository.save(position);
		String id = savedPos.getId();

		var existsById = positionRepository.existsById(id);
		assertThat(existsById).isTrue();

		Optional<Position> pos = positionRepository.findById(id);
		pos.ifPresent(v -> {
			assertThat(v.getTrackId()).isEqualTo("track_1");
			assertThat(v.getUserId()).isEqualTo("user_1");
			assertThat(v.getLatitude()).isEqualTo(45.5);
			assertThat(v.getLongitude()).isEqualTo(1.5);
		});
	}

	@Test
	@Tag("persistence")
	@Order(2)
	void whenUpdatingPosition_thenAvailableOnRetrieval() {
		Position position = new Position(48.2, 1.3, Instant.now().toEpochMilli(), "track_2", "user_2");
		Position savedPos = positionRepository.save(position);
		String id = savedPos.getId();

		var positionById = positionRepository.findById(id);
		positionById.ifPresent(p -> {
			assertThat(p.getLatitude()).isEqualTo(48.2);
		});

		positionById.get().setLatitude(49.5);
		var updatedPos = positionRepository.save(positionById.get());

		positionById = positionRepository.findById(id);
		positionById.ifPresent(p -> {
			assertThat(p.getLatitude()).isEqualTo(49.5);
		});
	}

	@Test
	@Tag("geo")
	@Order(1)
	void whenAddPosition_thenAvailableOnRetrieval() {
		var latitude = 43.53783457706302;
		var longitude = 1.4808604434856267;

		geoOperations.add("gym31", new Point(longitude, latitude ), "Basic Fit:31520:Ramonville");
		var positions = geoOperations.position("gym31", "Basic Fit:31520:Ramonville");

		var lg1 = Double.parseDouble(String.format(Locale.US, "%.5f", longitude));
		var lg2 = Double.parseDouble(String.format(Locale.US, "%.5f", positions.get(0).getX()));
		assertThat(lg1).isEqualTo(lg2);

		var lt1 = Double.parseDouble(String.format(Locale.US, "%.5f", latitude));
		var lt2 = Double.parseDouble(String.format(Locale.US, "%.5f", positions.get(0).getY()));
		assertThat(lt1).isEqualTo(lt2);
	}

	@Test
	@Tag("geo")
	@Order(2)
	void testGeoAdd() {
		geoOperations.add("gym31", new Point(1.4808604434856267, 43.53783457706302), "Basic Fit:31520:Ramonville");
		geoOperations.add("gym31", new Point(1.4950804686212287, 43.52346359972895), "Movida:31320:Castanet-Tolosan");

		var positions = geoOperations.position("gym31", "Basic Fit:31520:Ramonville", "Movida:31320:Castanet-Tolosan");
		assertThat(positions.size()).isEqualTo(2);

		positions.forEach(p -> {
			System.out.println("lg: " + p.getX() + " - lt: " + p.getY());
		});
	}

	@Test
	@Tag("geo")
	@Order(3)
	void testGeoDist() {
		var distance = geoOperations.distance("gym31",
				"Basic Fit:31520:Ramonville",
				"Movida:31320:Castanet-Tolosan",
				RedisGeoCommands.DistanceUnit.KILOMETERS);

		assertThat(distance.getValue()).isBetween(1.5, 2.0);
	}

	@Test
	@Tag("geo")
	@Order(4)
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
	@Order(5)
	void testGeoRadiusByMember_whenDistanceLower_2kms_thenReturn_NoMembers() {
		var geoResults = geoOperations.radius("gym31",
				"Basic Fit:31520:Ramonville",
				new Distance(1.9, RedisGeoCommands.DistanceUnit.KILOMETERS));

		assertThat(geoResults.getContent().size()).isEqualTo(1);
	}

	@Test
	@Tag("geo")
	@Order(6)
	void testGeoRadiusByMember_whenDistanceEquals_2kms_thenReturn_1Member() {
		var geoResults = geoOperations.radius("gym31",
				"Basic Fit:31520:Ramonville",
				new Distance(2.0, RedisGeoCommands.DistanceUnit.KILOMETERS));

		assertThat(geoResults.getContent().size()).isEqualTo(2);
	}
}
