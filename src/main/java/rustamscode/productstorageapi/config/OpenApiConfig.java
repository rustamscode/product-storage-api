package rustamscode.productstorageapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Product storage API",
                description = "API for managing products in the storage",
                contact = @Contact(
                        name = "Rustamscode"
                )
        )
)
public class OpenApiConfig {
}
