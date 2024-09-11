package com.talhaunal.backend.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.talhaunal.backend.controller.response.ErrorDto;
import com.talhaunal.backend.exception.BusinessException;
import com.talhaunal.backend.exception.DomainNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        System.out.println(ex.getMessage());
        System.out.println(ex.getClass().getName());
        return new ResponseEntity<>(new ErrorDto("bad request"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<ErrorDto> handleDomainNotFoundException(DomainNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorDto> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return new ResponseEntity<>(new ErrorDto("not authorized"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleBusinessException(BusinessException ex) {
        return new ResponseEntity<>(new ErrorDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ErrorDto> handleJsonMappingException(JsonMappingException ex) {
        return new ResponseEntity<>(new ErrorDto("request could not be parsed"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<String>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add( error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add( error.getDefaultMessage());
        }

        ErrorDto apiError = new ErrorDto(String.join(", ", errors));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

}
