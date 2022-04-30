package fr.vocaltech.spring.redis.repositories;

import fr.vocaltech.spring.redis.models.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PositionDaoImpl implements PositionDao {
    @Autowired
    private PositionRepository positionRepository;

    @Override
    public void deleteAllByUserId(String userId) {
        List<String> ids = new ArrayList<>();
        List<Position> positionsByUserId = positionRepository.findByUserId(userId);

        positionsByUserId.forEach(p -> ids.add(p.getId()));

        positionRepository.deleteAllById(ids);
    }
}
