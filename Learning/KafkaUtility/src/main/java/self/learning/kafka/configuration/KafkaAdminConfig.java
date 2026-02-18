package self.learning.kafka.configuration;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaAdminConfig {
    @Value("${kafka.bootstrap.server}")
    private String bootstrapServers;

    @Value("${kafka.request.timeout}")
    private String requestTimeout;

    @Value("${kafka.retries}")
    private String retries;

    @Bean
    public AdminClient adminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        props.put(AdminClientConfig.RETRIES_CONFIG, retries);
        return AdminClient.create(props);
    }
}
