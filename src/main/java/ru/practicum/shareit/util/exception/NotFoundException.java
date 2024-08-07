package ru.practicum.shareit.util.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final Long entityId;
    private final Class<?> entity;

    public NotFoundException(Long entityId, Class<?> entity) {
        super();
        this.entityId = entityId;
        this.entity = entity;
    }
}
