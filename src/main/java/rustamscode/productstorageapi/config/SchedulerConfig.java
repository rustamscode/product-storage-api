package rustamscode.productstorageapi.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("dev")
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "spring.scheduling.enabled")
public class SchedulerConfig {
}
