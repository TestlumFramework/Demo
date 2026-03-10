package com.knubisoft.testapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final LocalDateTime timestamp;
}
