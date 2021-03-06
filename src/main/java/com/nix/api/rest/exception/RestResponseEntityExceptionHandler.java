package com.nix.api.rest.exception;

import com.nix.api.rest.controller.UserRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(basePackageClasses = UserRestController.class)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = {NotValidUserException.class})
    protected ResponseEntity<Object> handleNotValidUserException(NotValidUserException e,
                                                                 WebRequest request) {

        MultiValueMap<String, String> errors = new LinkedMultiValueMap<>();

        e.getErrors().getFieldErrors()
                .forEach(err -> errors.add(err.getField(),
                        messageSource.getMessage(err, LocaleContextHolder.getLocale())));

        return handleExceptionInternal(e, errors,
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e,
                                                                 WebRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return handleExceptionInternal(e, null, headers,
                HttpStatus.NOT_FOUND, request);
    }

}
