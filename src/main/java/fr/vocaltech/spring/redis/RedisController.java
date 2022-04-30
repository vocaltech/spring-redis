package fr.vocaltech.spring.redis;

import fr.vocaltech.spring.redis.repositories.PositionDao;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;

import java.util.List;
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
    private PositionDao positionDao;

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
    public ResponseEntity<PositionDto> createPosition(@RequestBody PositionDto positionDto) {
        Position newPosition = convertToPositionModel(positionDto);
        Position savedPos = positionRepository.save(newPosition);

        return new ResponseEntity<>(convertToPositionDto(savedPos), HttpStatus.CREATED);
    }

    @PostMapping(value = "/bulk")
    public ResponseEntity<Iterable<Position>> createBulkPositions(@RequestBody List<Position> bulkPositions) {
        Iterable<Position> bulkSavedPositions = positionRepository.saveAll(bulkPositions);

        return new ResponseEntity<>(bulkSavedPositions, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{positionId}")
    public ResponseEntity<Position> findPositionById(@PathVariable("positionId") String positionId) {
        Optional<Position> foundPosition = positionRepository.findById(positionId);

        if (foundPosition.isPresent()) {
            return new ResponseEntity<>(foundPosition.get(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "userid/{userId}")
    public ResponseEntity<List<Position>> findPositionsByUserId(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(positionRepository.findByUserId(userId), HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/{positionId}" )
    public ResponseEntity<String> deletePositionById(@PathVariable("positionId") String positionId) {
        positionRepository.deleteById(positionId);

        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/")
    public ResponseEntity<String> deleteAllPositions() {
        positionRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @DeleteMapping(value = "userid/{userId}")
    public ResponseEntity<String> deletePositionsByUserId(@PathVariable("userId") String userId) {
        positionDao.deleteAllByUserId(userId);
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
