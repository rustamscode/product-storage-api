package rustamscode.productstorageapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import rustamscode.productstorageapi.config.RestProperties;

@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties(RestProperties.class)
public class ProductStorageApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductStorageApiApplication.class, args);
  }

}
