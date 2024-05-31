package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class ContactDataEmptyException extends BaseException {
    public ContactDataEmptyException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
