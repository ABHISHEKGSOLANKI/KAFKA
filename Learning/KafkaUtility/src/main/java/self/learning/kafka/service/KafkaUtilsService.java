package self.learning.kafka.service;

import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import self.learning.kafka.configuration.KafkaAdminConfig;
import self.learning.kafka.dto.KafkaMetadata;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaUtilsService {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private AdminClient adminClient;

    public String getTopicList(){
        return kafkaTemplate.getKafkaAdmin().clusterId();
//        return kafkaTemplate.getDefaultTopic();
    }

    public void createTopic(KafkaMetadata kafkaMetadata) {

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

            System.out.println("✅ Topic created successfully: " + kafkaMetadata.topic());
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

}
