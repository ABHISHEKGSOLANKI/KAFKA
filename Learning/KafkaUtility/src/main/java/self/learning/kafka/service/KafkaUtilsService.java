package self.learning.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaUtilsService {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    public String getTopicList(){
        return kafkaTemplate.getKafkaAdmin().clusterId();
//        return kafkaTemplate.getDefaultTopic();
    }
}
