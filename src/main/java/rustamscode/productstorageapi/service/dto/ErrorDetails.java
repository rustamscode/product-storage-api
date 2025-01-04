package rustamscode.productstorageapi.service.dto;


import lombok.Builder;
import lombok.Value;

/**
 * Represents information about an error that occurred during execution.
 */

@Value
@Builder
public class ErrorDetails {
    String message;
    String exception;
    String source;
}
