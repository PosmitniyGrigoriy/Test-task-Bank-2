package ru.bank.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public BaseException(HttpStatus status, String message) {
        this.status= status;
        this.message = message;
    }
}