package fr.vocaltech.spring.redis;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Optional;

import fr.vocaltech.spring.redis.models.Position;
import fr.vocaltech.spring.redis.dto.PositionDto;
import fr.vocaltech.spring.redis.repositories.PositionRepository;

@RestController
@RequestMapping("/positions")
public class RedisController {
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private RedisOperations<String, String> operations;

    @Autowired
    private ModelMapper modelMapper;

    private GeoOperations<String, String> geoOperations;

    @GetMapping("")
    public ResponseEntity<Iterable<Position>> findAllPositions() {
        return new ResponseEntity<>(positionRepository.findAll(), HttpStatus.FOUND);
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

    @DeleteMapping(value = "/{userId}" )
    public ResponseEntity<String> deletePositionById(@PathVariable("userId") String userId) {
        positionRepository.deleteById(userId);

        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @PutMapping(value = "/{positionId}")
    public ResponseEntity<PositionDto> updatePositionById(
            @PathVariable("positionId") String positionId,
            @RequestBody PositionDto positionDto) {

        positionDto.setId(positionId);

        Position newPosition = convertToPositionModel(positionDto);

        Optional<Position> foundPosition = positionRepository.findById(positionId);
        return foundPosition
                .map(pos -> {
                    Position savedPosition = positionRepository.save(newPosition);
                    return new ResponseEntity<>(convertToPositionDto(savedPosition), HttpStatus.CREATED);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private PositionDto convertToPositionDto(Position position) {
        return modelMapper.map(position, PositionDto.class);
    }

    private Position convertToPositionModel(PositionDto positionDto) {
        return modelMapper.map(positionDto, Position.class);
    }
}
