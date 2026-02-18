package self.learning.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import self.learning.kafka.dto.KafkaMetadata;
import self.learning.kafka.service.KafkaUtilsService;

import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/kafka/v1")
public class KafKaUtilsController {

    @Autowired
    private KafkaUtilsService kafkaUtilsService;

    @PostMapping("/create")
    public ResponseEntity<KafkaMetadata> createTopic(@RequestBody KafkaMetadata kafkaMetadata){
        KafkaMetadata updatedMetadata = kafkaUtilsService.createTopic(kafkaMetadata);
        return new ResponseEntity<>(updatedMetadata, HttpStatus.CREATED);
    }

//    @PatchMapping("/update")
//    public ResponseEntity<KafkaMetadata> updateTopic(@RequestBody KafkaMetadata kafkaMetadata){
//        KafkaMetadata updateKafkaMetadata = kafkaUtilsService.update(kafkaMetadata);
//        return new ResponseEntity<>(updateKafkaMetadata, HttpStatus.ACCEPTED);
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTopic(){
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/topics")
    public ResponseEntity<Set<String>> getTopics(){
        Set<String> topicList = kafkaUtilsService.getTopicList();
        return new ResponseEntity<>(topicList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/topics/{name}")
    public ResponseEntity<Map<String, TopicDescription>> isTopicExist(@PathVariable String name ,@RequestBody String topic){
        Map<String, TopicDescription> kafkaMetadata = kafkaUtilsService.isTopicExist(topic);
        return new ResponseEntity<>(kafkaMetadata, HttpStatus.OK);
    }
}
