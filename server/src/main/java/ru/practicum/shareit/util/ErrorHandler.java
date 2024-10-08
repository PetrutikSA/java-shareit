package ru.practicum.shareit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.util.exception.AccessForbiddenException;
import ru.practicum.shareit.util.exception.BadRequestException;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.util.exception.UserConflictException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        String details = String.format("%s with id=%d not found", exception.getEntity().getName(),
                exception.getEntityId());
        log.warn(details, exception);
        return ErrorResponse.create(exception, HttpStatus.NOT_FOUND, details);
    }

    @ExceptionHandler
    public ErrorResponse handleUserConflictException(UserConflictException exception) {
        log.warn(exception.getMessage(), exception);
        return ErrorResponse.create(exception, HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse handleAccessForbiddenException(AccessForbiddenException exception) {
        log.warn(exception.getMessage(), exception);
        return ErrorResponse.create(exception, HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse handleBadRequestException(BadRequestException exception) {
        log.warn(exception.getMessage(), exception);
        return ErrorResponse.create(exception, HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
