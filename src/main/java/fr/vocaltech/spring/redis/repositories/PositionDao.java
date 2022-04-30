package fr.vocaltech.spring.redis.repositories;

import org.springframework.stereotype.Repository;

@Repository
public interface PositionDao {
    void deleteAllByUserId(String userId);
}
