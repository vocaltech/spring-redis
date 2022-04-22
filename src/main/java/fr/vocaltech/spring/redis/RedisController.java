package fr.vocaltech.spring.redis;

import fr.vocaltech.spring.redis.models.Position;
import fr.vocaltech.spring.redis.repositories.PositionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/positions")
public class RedisController {
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private RedisOperations<String, String> operations;

    private GeoOperations<String, String> geoOperations;

    @GetMapping("/")
    public String index() {
        return "{ \"message\": \"RedisController\" }";
    }
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Position> createPosition(@RequestBody Position position) {
        Position savedPos = positionRepository.save(position);
        String id = savedPos.getId();

        System.out.println("saved id: " + id);

        return new ResponseEntity<>(savedPos, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Position> findPositionById(@PathVariable("userId") String userId) {
        Optional<Position> foundPosition = positionRepository.findById(userId);

        if (foundPosition.isPresent()) {
            return new ResponseEntity<>(foundPosition.get(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
