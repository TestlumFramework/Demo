package com.knubisoft.testapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> handleEntityBadRequest(ResourceAlreadyExistsException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = QueueNotFoundException.class)
    public ResponseEntity<Object> handleQueueNotFound(QueueNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        logEx(ex);
        String exMessage = getMessage(ex);
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; ", exMessage + " [", "]"));
        return buildResponse(HttpStatus.BAD_REQUEST, ex, errorMessage);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternal(Exception ex) {
        logEx(ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus httpStatus, Exception ex) {
        String errorMessage = getMessage(ex);
        return buildResponse(httpStatus, ex, errorMessage);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus httpStatus, Exception ex, String errorMessage) {
        logEx(ex);
        ErrorResponse errorResponse = new ErrorResponse(httpStatus, errorMessage, LocalDateTime.now());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    private String getMessage(Throwable ex) {
        if (ex == null) {
            return StringUtils.EMPTY;
        }
        String message = ex.getMessage();
        if (message != null) {
            return message;
        }
        return getMessage(ExceptionUtils.getRootCause(ex));
    }

    private void logEx(Exception ex) {
        log.error("Handle an exception: ", ex);
    }
}
