package tn.enetcom.maintenance.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice

public class GestionErreurs {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> erreurMetier(ResponseStatusException ex) {
        String message = ex.getReason() != null ? ex.getReason() : "Erreur";
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("erreur", message));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
        Map<String, String> details = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> details.put(e.getField(), e.getDefaultMessage()));

        Map<String, Object> corps = new LinkedHashMap<>();
        corps.put("erreur", "Données invalides");
        corps.put("details", details);
        return ResponseEntity.badRequest().body(corps);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonIllisible(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erreur",
                "Requête illisible : vérifie le format du JSON, les dates (AAAA-MM-JJ) et les valeurs autorisées"));
    }
}
