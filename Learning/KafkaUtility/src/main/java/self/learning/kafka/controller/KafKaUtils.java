package self.learning.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import self.learning.kafka.dto.KafkaMetadata;
import self.learning.kafka.service.KafkaUtilsService;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/kafka")
public class KafKaUtils {

    @Autowired
    private KafkaUtilsService kafkaUtilsService;

    @PostMapping("/create")
    public ResponseEntity<String> createTopic(@RequestBody KafkaMetadata kafkaMetadata){
        kafkaUtilsService.createTopic(kafkaMetadata);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/topic")
    public ResponseEntity<Map<String, TopicDescription>> isTopicExist(@RequestBody String topic){
        Map<String, TopicDescription> kafkaMetadata = kafkaUtilsService.isTopicExist(topic);
        return new ResponseEntity<>(kafkaMetadata, HttpStatus.OK);
    }
    @GetMapping("/topiclist")
    public ResponseEntity<String> getTopicList(@RequestBody String data){
        data = kafkaUtilsService.getTopicList();
        System.out.println(data);
        return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
    }
}
