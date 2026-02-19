package self.learning.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import self.learning.kafka.dto.KafkaMetadata;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
            e.getMessage();
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

    public List<String> isTopicExist(String topic) {
        try {
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(false); // ignore __consumer_offsets

            Set<String> names = adminClient.listTopics(options)
                    .names()
                    .get();

            if(names.contains(topic))
                return List.of(topic);

        } catch (Exception e) {
            throw new RuntimeException("Failed to check topic existence", e);
        }
        return null;
    }

    public Map<String, TopicDescription> describeTopics(List<String> topics) {
        try {
            DescribeTopicsResult result = adminClient.describeTopics(topics);
            return result.all().get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to describe topics", e);
        }
    }

    public void deleteTopic(Set<String> topicList) {
        topicList.forEach(topicName ->{
            DeleteTopicsResult result =
                    adminClient.deleteTopics(Collections.singletonList(topicName));
            // Wait for operation to complete
            try {
                result.all().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

            log.info("Topic '{}' deleted successfully", topicName);
        });
    }

    public void getKafkaBrokers() {
        DescribeClusterResult cluster = adminClient.describeCluster();

        // Get all brokers registered in cluster metadata
        Collection<Node> nodes = null;
        try {
            nodes = cluster.nodes().get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        log.info("==== Kafka Brokers in Cluster ====");

        for (Node node : nodes) {
            log.info(
                    "Broker ID: " + node.id() +
                            " | Host: " + node.host() +
                            " | Port: " + node.port() +
                            " | Rack: " + node.rack()
            );
        }

        log.info("\n==== ACTIVE Brokers ====");

        // If node is returned here → it is ACTIVE
        nodes.forEach(node ->
                log.info("ACTIVE → " + node.host() + ":" + node.port())
        );

    }

//    public KafkaMetadata update(KafkaMetadata kafkaMetadata) {
//        KafkaMetadata updateKafkaMetadata = null;
//        return n
//    }
}
