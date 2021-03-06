package fr.vocaltech.spring.redis.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("Position")
@Data
@ToString
@NoArgsConstructor
public class Position implements Serializable {
    @Id
    private String id;

    private double latitude;
    private double longitude;
    private long time;
    private String trackId;
    @Indexed
    private String userId;

    public Position(double latitude, double longitude, long time, String trackId, String userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.trackId = trackId;
        this.userId = userId;
    }
}
