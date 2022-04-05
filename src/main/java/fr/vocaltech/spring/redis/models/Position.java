package fr.vocaltech.spring.redis.models;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Position")
@Data
@ToString
public class Position implements Serializable {
    @Id
    private String id;

    private double latitude;
    private double longitude;
    private long time;
    private String trackId;
    private String userId;

    public Position(double latitude, double longitude, long time, String trackId, String userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.trackId = trackId;
        this.userId = userId;
    }
}
