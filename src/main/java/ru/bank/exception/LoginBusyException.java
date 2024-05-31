package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class LoginBusyException extends BaseException {
    public LoginBusyException(String errorMessage) {
        super(HttpStatus.CONFLICT, errorMessage);
    }
}
