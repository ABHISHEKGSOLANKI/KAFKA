package self.learning.kafka.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

public record KafkaMetadata(
        String topic,
        int partition,
        Map<String, String> headers,
        Short replificationFactor,
        String retention,
        LocalDateTime timeStamp
) {
}
