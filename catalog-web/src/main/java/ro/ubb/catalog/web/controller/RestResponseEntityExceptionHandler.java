package ro.ubb.catalog.web.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by radu.
 * <p>
 * http://www.baeldung.com/exception-handling-for-rest-with-spring
 */

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);


    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleGeneric(RuntimeException ex, WebRequest request) {
        log.trace("handleGeneric: ex={}", ex.getMessage());
        log.trace("Details: " + ex.toString());

        ApiError apiError =
                new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex.toString());

        log.trace("handleGeneric EXITED");

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.trace("handleMethodArgumentNotValid: ex={}", ex);

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST,
                        ex.getLocalizedMessage(),
                        ex.getBindingResult().toString());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}

@Getter
class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    ApiError(HttpStatus status, String message, String error) {
        this(status, message, Arrays.asList(error));
    }
}
