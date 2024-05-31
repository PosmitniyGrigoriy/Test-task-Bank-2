package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class FieldInPersonalDataIsIncorrectlyFilledInException extends BaseException {
    public FieldInPersonalDataIsIncorrectlyFilledInException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
