package self.learning.kafka.dto;

import java.time.Instant;
import java.util.Map;

public record KafkaMetadata(
        String topic,
        int partition,
        long offset,
        long timestamp,
        Instant timestampInstant,
        String timestampType,
        int serializedKeySize,
        int serializedValueSize,
        Map<String, String> headers,
        String consumerGroup,
        String key,
        Short replificationFactor,
        String retention
) {
}
