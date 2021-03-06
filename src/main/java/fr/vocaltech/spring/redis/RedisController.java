package fr.vocaltech.spring.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.vocaltech.spring.redis.repositories.PositionDao;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import fr.vocaltech.spring.redis.models.Position;
import fr.vocaltech.spring.redis.dto.PositionDto;
import fr.vocaltech.spring.redis.repositories.PositionRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
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

    @GetMapping(path = "positions")
    public ResponseEntity<Iterable<Position>> findAllPositions() {
        return new ResponseEntity<>(positionRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<String> getDefault() throws JsonProcessingException {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("msg", "hello");

        String jsonStr = new ObjectMapper().writeValueAsString(hashMap);

        return new ResponseEntity<>(jsonStr, HttpStatus.OK);
    }
    @PostMapping(
            path = "positions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PositionDto> createPosition(@RequestBody PositionDto positionDto) {
        Position newPosition = convertToPositionModel(positionDto);
        Position savedPos = positionRepository.save(newPosition);

        return new ResponseEntity<>(convertToPositionDto(savedPos), HttpStatus.CREATED);
    }

    @PostMapping(value = "/positions/bulk")
    public ResponseEntity<Iterable<PositionDto>> createBulkPositions(@RequestBody List<PositionDto> bulkPositionsDto) {
        List<Position> bulkPositions = new ArrayList<>();

         bulkPositions = bulkPositionsDto
                .stream()
                .map(this::convertToPositionModel)
                .collect(Collectors.toList());

        Iterable<Position> bulkSavedPositions = positionRepository.saveAll(bulkPositions);

        List<PositionDto> bulkSavedPositionsDto = StreamSupport.stream(bulkSavedPositions.spliterator(), false)
                .map(this::convertToPositionDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(bulkSavedPositionsDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/positions/{positionId}")
    public ResponseEntity<Position> findPositionById(@PathVariable("positionId") String positionId) {
        Optional<Position> foundPosition = positionRepository.findById(positionId);

        if (foundPosition.isPresent()) {
            return new ResponseEntity<>(foundPosition.get(), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/positions/userid/{userId}")
    public ResponseEntity<List<Position>> findPositionsByUserId(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(positionRepository.findByUserId(userId), HttpStatus.FOUND);
    }

    @DeleteMapping(value = "/positions/{positionId}" )
    public ResponseEntity<String> deletePositionById(@PathVariable("positionId") String positionId) {
        positionRepository.deleteById(positionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/positions")
    public ResponseEntity<String> deleteAllPositions() {
        positionRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/positions/userid/{userId}")
    public ResponseEntity<String> deletePositionsByUserId(@PathVariable("userId") String userId) {
        positionDao.deleteAllByUserId(userId);
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @PutMapping(value = "/positions/{positionId}")
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
