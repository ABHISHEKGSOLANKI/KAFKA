package self.learning.kafka.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${react.base.url}")
    private String baseUrl;

    @Value("${react.allowed.methods}")
    private String methods;

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins(baseUrl)
                    .allowedMethods(methods.split(","))
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
        }

}

