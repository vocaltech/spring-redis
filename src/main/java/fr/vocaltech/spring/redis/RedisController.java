package fr.vocaltech.spring.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @GetMapping("/")
    public String index() {
        return "{ \"message\": \"RedisController\" }";
    }
}
