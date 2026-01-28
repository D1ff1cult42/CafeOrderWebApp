package org.example.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackRestController {
    @GetMapping("/menu")
    public ResponseEntity<?> menuFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Menu service is currently unavailable. Please try again later.");
    }

    @GetMapping("/order")
    public ResponseEntity<?> orderFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Order service is currently unavailable. Please try again later.");
    }

    @GetMapping("/payment")
    public ResponseEntity<?> paymentFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Payment service is currently unavailable. Please try again later.");
    }
}
