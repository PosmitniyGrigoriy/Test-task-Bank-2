package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class TokenDurationIncorrectException extends BaseException {
    public TokenDurationIncorrectException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
