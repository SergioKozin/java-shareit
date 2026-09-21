package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplicatedDataException extends RuntimeException {
    public DuplicatedDataException(String message) {
        super(message);
        log.error(message);
    }
}
