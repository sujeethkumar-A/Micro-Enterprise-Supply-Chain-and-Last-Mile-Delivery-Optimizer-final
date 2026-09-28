package com.example.supplychain.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String accept = request.getHeader("Accept");
        return uri.startsWith("/api") || (accept != null && accept.contains("application/json"));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleNotFound(ResourceNotFoundException ex, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "Not Found");
            body.put("message", ex.getMessage());
            body.put("path", request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InsufficientStockException.class)
    public Object handleInsufficientStock(InsufficientStockException ex, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Insufficient Stock");
            body.put("message", ex.getMessage());
            body.put("path", request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("errorCode", "400");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request, Model model) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        if (isApiRequest(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Validation Error");
            body.put("message", "Input validation failed");
            body.put("errors", errors);
            body.put("path", request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        model.addAttribute("errorCode", "400");
        model.addAttribute("errorMessage", "Validation failed: " + errors.values().toString());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, HttpServletRequest request, Model model) {
        if (isApiRequest(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            body.put("error", "Internal Server Error");
            body.put("message", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
            body.put("path", request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred.");
        return "error";
    }
}
