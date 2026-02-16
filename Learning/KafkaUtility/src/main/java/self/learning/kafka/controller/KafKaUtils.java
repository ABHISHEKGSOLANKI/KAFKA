package self.learning.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import self.learning.kafka.service.KafkaUtilsService;

@RestController
@Slf4j
public class KafKaUtils {

    @Autowired
    private KafkaUtilsService kafkaUtilsService;
    @GetMapping("kafka/topiclist")
    public ResponseEntity<String> getTopicList(@RequestBody String data){
        data = kafkaUtilsService.getTopicList();
        System.out.println(data);
        return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
    }
}
