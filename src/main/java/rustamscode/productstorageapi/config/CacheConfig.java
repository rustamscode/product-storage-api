package rustamscode.productstorageapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final RestProperties restProperties;

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        final Integer expirationTime = restProperties.getCurrencyServiceClient()
                .getCache()
                .getExpirationTime();
        final Integer initialCapacity = restProperties.getCurrencyServiceClient()
                .getCache()
                .getInitialCapacity();

        return Caffeine.newBuilder()
                .initialCapacity(initialCapacity)
                .expireAfterAccess(expirationTime, TimeUnit.SECONDS);
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        final CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeine);

        return caffeineCacheManager;
    }
}
