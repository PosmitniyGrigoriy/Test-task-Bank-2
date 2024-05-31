package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class InvalidContactDataException extends BaseException {
    public InvalidContactDataException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
