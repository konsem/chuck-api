package io.chucknorris.api.health;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Optional;

@RestController
public class HealthController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/health")
    public Map<String, String> show() {
        var runtime = ManagementFactory.getRuntimeMXBean();

        return Map.of(
                "status", "UP",
                "start_time", Optional.of(runtime.getStartTime()).map(String::valueOf).orElse("undefined"),
                "uptime", Optional.of(runtime.getUptime()).map(String::valueOf).orElse("undefined"));
    }
}
