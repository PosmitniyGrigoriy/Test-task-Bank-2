package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

import static ru.bank.constants.Swagger.ERROR_MESSAGE;

public class AuthenticationIsNotViaJWTException extends BaseException {
    public AuthenticationIsNotViaJWTException() {
        super(HttpStatus.FORBIDDEN, ERROR_MESSAGE);
    }
}
