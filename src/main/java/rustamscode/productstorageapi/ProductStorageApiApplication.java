package rustamscode.productstorageapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ProductStorageApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductStorageApiApplication.class, args);
    }

}
