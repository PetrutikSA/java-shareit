package ru.practicum.shareit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    public ErrorResponse handleBadRequestException(IllegalArgumentException exception) {
        log.warn(exception.getMessage(), exception);
        return ErrorResponse.create(exception, HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
