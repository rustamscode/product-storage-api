package rustamscode.productstorageapi.config;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rustamscode.productstorageapi.service.dto.ErrorDetails;
import rustamscode.productstorageapi.exception.NonUniqueProductNumberException;
import rustamscode.productstorageapi.exception.ProductNotFoundException;

import static java.util.stream.Collectors.joining;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDetails productNotFoundException(ProductNotFoundException exception) {
        log.error(exception.getMessage());

        return ErrorDetails.builder()
                .message(exception.getMessage())
                .exception(exception.getClass().getSimpleName())
                .source(exception.getStackTrace()[0].getClassName())
                .build();
    }

    @ExceptionHandler(NonUniqueProductNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails nonUniqueProductNumberException(NonUniqueProductNumberException exception) {
        log.error(exception.getMessage());

        return ErrorDetails.builder()
                .message(exception.getMessage())
                .exception(exception.getClass().getSimpleName())
                .source(exception.getStackTrace()[0].getClassName())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDetails methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());

        String message = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(joining("; "));

        String className = exception.getStackTrace()[0].getClassName();

        return ErrorDetails.builder()
                .message(message)
                .exception(exception.getClass().getSimpleName())
                .source(className.substring(className.lastIndexOf(".") + 1))
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorDetails constraintViolationException(ConstraintViolationException exception) {
        log.error(exception.getMessage());

        String message = exception.getConstraintViolations()
                .stream()
                .map(Object::toString)
                .collect(joining(" ;"));

        String className = exception.getStackTrace()[0].getClassName();

        return ErrorDetails.builder()
                .message(message)
                .exception(exception.getClass().getSimpleName())
                .source(className.substring(className.lastIndexOf(".") + 1))
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDetails handleGlobalException(Exception exception) {
        log.error(exception.getMessage());

        return ErrorDetails.builder()
                .message(exception.getMessage())
                .exception(exception.getClass().getSimpleName())
                .source(exception.getStackTrace()[0].getClassName())
                .build();
    }
}
