package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class InvalidBalanceIncreaseArgumentsException extends BaseException {
    public InvalidBalanceIncreaseArgumentsException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
