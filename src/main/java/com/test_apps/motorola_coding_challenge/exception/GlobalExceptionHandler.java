package com.test_apps.motorola_coding_challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.test_apps.motorola_coding_challenge.exception.ExceptionMessages.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleFileNotFound(FileNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", FILE_NOT_FOUND_ERROR);
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(FileNameRequiredException.class)
    public ResponseEntity<Map<String, Object>> handleFileNameRequired(FileNameRequiredException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", FILE_NAME_REQUIRED_ERROR);
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(FileSystemException.class)
    public ResponseEntity<Map<String, Object>> handleFileSystem(FileSystemException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", FILE_SYSTEM_ERROR);
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurity(SecurityException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", INVALID_FILE_NAME_ERROR);
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleFallback(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", UNEXPECTED_ERROR);
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
