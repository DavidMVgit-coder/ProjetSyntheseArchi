package api.error;

import application.GetHero.HeroNotFoundException;
import application.GetHero.InvalidHeroIdException;
import application.InvalidHeroInputException;
import domain.exception.DomainException;
import domain.exception.HeroAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HeroAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateHero(
            HeroAlreadyExistsException exception, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, "HERO_ALREADY_EXISTS", exception.getMessage(), request);
    }

    @ExceptionHandler(HeroNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleHeroNotFound(
            HeroNotFoundException exception, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "HERO_NOT_FOUND", exception.getMessage(), request);
    }

    @ExceptionHandler({DomainException.class, InvalidHeroInputException.class, InvalidHeroIdException.class})
    public ResponseEntity<ApiErrorResponse> handleInvalidRequest(
            RuntimeException exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", exception.getMessage(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableBody(
            HttpMessageNotReadableException exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "Le corps JSON est invalide.", request);
    }

    private ResponseEntity<ApiErrorResponse> error(
            HttpStatus status, String code, String message, HttpServletRequest request) {
        ApiErrorResponse body = new ApiErrorResponse(
                Instant.now(), status.value(), code, message, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
