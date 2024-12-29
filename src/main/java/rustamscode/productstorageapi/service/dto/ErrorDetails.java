package rustamscode.productstorageapi.service.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Represents information about an error that occurred during execution.
 */

@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDetails {
    String message;
    String exception;
    String source;
}
