package fr.vocaltech.spring.redis.dto;

import lombok.Data;

@Data
public class PositionDto {
    private String id;
    private double latitude;
    private double longitude;
    private long time;
    private String trackId;
    private String userId;
}
