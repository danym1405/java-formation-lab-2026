package com.indra.catalog.products.web;

import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.indra.catalog.products.domain.exception.ProductNotFoundException;
import com.indra.catalog.products.web.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;


@RestControllerAdvice 
public class ApiExcepctionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException exception, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(Instant.now(), 404,"PRODUCT_NOT_FOUND",
                                List.of(exception.getMessage()), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {

        List<String> errors =exception.getBindingResult().getFieldErrors().stream().map(error ->error.getField() + ": "+ 
                            error.getDefaultMessage()).toList();

        return ResponseEntity.badRequest().body(new ErrorResponse(Instant.now(), 400, "VALIDATION_ERROR", errors, request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new ErrorResponse(Instant.now(),
                                500, "INTERNAL_ERROR", List.of(exception.getMessage()), request.getRequestURI()));
    }
}