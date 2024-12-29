package rustamscode.productstorageapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

  @Value("${spring.cache.expiration-time}")
  private Integer EXPIRATION_TIME;

  @Value("${spring.cache.initial-capacity}")
  private Integer INITIAL_CAPACITY;

  @Bean
  public Caffeine<Object, Object> caffeineConfig() {
    return Caffeine.newBuilder()
        .initialCapacity(INITIAL_CAPACITY)
        .expireAfterAccess(EXPIRATION_TIME, TimeUnit.SECONDS);
  }

  @Bean
  public CacheManager cacheManager(Caffeine caffeine) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
    caffeineCacheManager.setCaffeine(caffeine);

    return caffeineCacheManager;
  }
}
