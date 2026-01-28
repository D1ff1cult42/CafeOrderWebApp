package org.example.paymentservice.controller.controllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.dto.response.ErrorResponse;
import org.example.paymentservice.exceptions.PaymentFailedException;
import org.example.paymentservice.exceptions.PaymentNotFoundException;
import org.example.paymentservice.exceptions.RefundFailedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PaymentRestControllerAdvice {

    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        log.error("Payment not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(System.currentTimeMillis()),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid argument: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RefundFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleRefundException(RefundFailedException ex){
        log.error("Refund failed: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
                new Date(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                errorMessage.isEmpty() ? "Validation Failed" : errorMessage
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handlePathNotFoundException(NoHandlerFoundException ex, HttpServletRequest request){
        log.error("Path not found: {}", ex.getMessage(), request.getRequestURI());
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(System.currentTimeMillis()),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage() + " for path: " + request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

   @ExceptionHandler(PaymentFailedException.class)
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ResponseEntity<ErrorResponse> handlePaymentException(PaymentFailedException ex) {
       log.error("Payment failed: {}", ex.getMessage());
       ErrorResponse errorResponse = new ErrorResponse(
               new Date(System.currentTimeMillis()),
               HttpStatus.BAD_REQUEST.value(),
               ex.getMessage()
       );
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
   }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("Runtime exception: {} for request '{}'", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(System.currentTimeMillis()),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) throws Exception {
        if (request.getRequestURI().startsWith("/actuator")){
            log.info("Catch actuator(NOT EXCEPTION)");
            throw ex;
        }
        log.error("Internal server error: {} for request '{}'", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                new Date(System.currentTimeMillis()),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
