package example.tournament.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchResourceException.class)
    private ResponseEntity<Object> handleNoSuchResourceException() {
        return ResponseEntity.notFound().build();
    }
}
