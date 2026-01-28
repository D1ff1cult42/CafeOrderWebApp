package org.example.paymentservice.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
public class RefundFailedException extends RuntimeException {
    public RefundFailedException(String message) {
        super(message);
    }
}
