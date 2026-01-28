package org.d1ff.menuservice.controller.controllerAdvice;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.d1ff.menuservice.dto.response.ErrorResponse;
import org.d1ff.menuservice.exception.ResourceAlreadyExistsException;
import org.d1ff.menuservice.exception.ResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PizzaRestControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("'%s' %s",
                        error.getField(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());

        String errorMessage = String.join("; ", errors);
        ErrorResponse errorResponse = new ErrorResponse(
                new java.util.Date(System.currentTimeMillis()),
                org.springframework.http.HttpStatus.BAD_REQUEST.value(),
                errorMessage.isEmpty() ? "Validation Failed" : errorMessage
        );
        return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        log.error("Resource already exists: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                new java.util.Date(System.currentTimeMillis()),
                org.springframework.http.HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundResourceException(ResourceNotFoundException ex){
        log.error("Resource not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                new java.util.Date(System.currentTimeMillis()),
                org.springframework.http.HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handlePathNotFoundException(NoHandlerFoundException ex, HttpServletRequest request){
        log.error("Path not found: {}", ex.getMessage(), request.getRequestURI());
        ErrorResponse errorResponse = new ErrorResponse(
                new java.util.Date(System.currentTimeMillis()),
                org.springframework.http.HttpStatus.NOT_FOUND.value(),
                ex.getMessage() + " for path: " + request.getRequestURI()
        );
        return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) throws Exception {
        if (request.getRequestURI().startsWith("/actuator")) {
            log.info("Catch actuator(NOT EXCEPTION)");
            throw ex;
        }
        log.error("Internal server error: {} for request '{}'", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                new java.util.Date(System.currentTimeMillis()),
                org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
