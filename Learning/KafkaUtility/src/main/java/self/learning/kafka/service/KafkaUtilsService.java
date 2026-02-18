package self.learning.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import self.learning.kafka.dto.KafkaMetadata;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaUtilsService {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private AdminClient adminClient;

    public Set<String> getTopicList() {
        try {
            return adminClient.listTopics()
                    .names()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KafkaMetadata createTopic(KafkaMetadata kafkaMetadata) {
        KafkaMetadata updatedMetadata = null;
        try {
            // Define topic
            NewTopic newTopic = new NewTopic(
                    kafkaMetadata.topic(),
                    kafkaMetadata.partition(),      // number of partitions
                    kafkaMetadata.replificationFactor()  // replication factor
            );

            // Optional configs (VERY useful in real systems)
            newTopic.configs(Collections.singletonMap(
                    "retention.ms", kafkaMetadata.retention() // 7 days retention
            ));

            // Create topic
            CreateTopicsResult result =
                    adminClient.createTopics(Collections.singleton(newTopic));

            // Wait for result
            result.all().get();

            log.info("✅ Topic created successfully: {}", kafkaMetadata.topic());
            updatedMetadata = new KafkaMetadata(
                    kafkaMetadata.topic(),
                    kafkaMetadata.partition(),
                    kafkaMetadata.headers(),
                    kafkaMetadata.replificationFactor(),
                    kafkaMetadata.retention(),
                    LocalDateTime.now()
            );
            return updatedMetadata;

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, TopicDescription> isTopicExist(String topic) {
        try {
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(false); // ignore __consumer_offsets

            Set<String> names = adminClient.listTopics(options)
                    .names()
                    .get();

            if(names.contains(topic))
                return describeTopics(List.of(topic));

        } catch (Exception e) {
            throw new RuntimeException("Failed to check topic existence", e);
        }
        return new HashMap<String, TopicDescription>();
    }

    public Set<String> listAllTopics() {
        try {
            return adminClient.listTopics()
                    .names()
                    .get();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch topics", e);
        }
    }

    public Map<String, TopicDescription> describeTopics(List<String> topics) {
        try {
            DescribeTopicsResult result = adminClient.describeTopics(topics);
            return result.all().get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to describe topics", e);
        }
    }

//    public KafkaMetadata update(KafkaMetadata kafkaMetadata) {
//        KafkaMetadata updateKafkaMetadata = null;
//        return n
//    }
}
