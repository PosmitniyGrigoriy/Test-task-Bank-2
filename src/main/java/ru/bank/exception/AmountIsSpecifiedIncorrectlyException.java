package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class AmountIsSpecifiedIncorrectlyException extends BaseException {
    public AmountIsSpecifiedIncorrectlyException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
