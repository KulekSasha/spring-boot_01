package com.nix.api.rest.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

public class NotValidUserException extends RuntimeException {

    @Getter
    private Errors errors;

    public NotValidUserException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }
}
