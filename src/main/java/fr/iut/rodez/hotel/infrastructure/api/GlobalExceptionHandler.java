package fr.iut.rodez.hotel.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Capture les exceptions levées par ton Domaine (Doublons, Statuts invalides...)
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> handleBusinessExceptions(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request / Règle Métier Violée");
        body.put("message", ex.getMessage()); // <-- Affiche ton message personnalisé
        body.put("path", "/invoices");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}