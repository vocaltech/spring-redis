package fr.vocaltech.spring.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.vocaltech.spring.redis.models.Position;

import java.util.List;

@Repository
public interface PositionRepository extends CrudRepository<Position, String> {
    List<Position> findByUserId(String userId);
}
