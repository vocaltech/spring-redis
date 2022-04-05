package fr.vocaltech.spring.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.vocaltech.spring.redis.models.Position;

@Repository
public interface PositionRepository extends CrudRepository<Position, String> {}
