package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class BankOperationStatusIsIncorrectlySpecifiedException extends BaseException {
    public BankOperationStatusIsIncorrectlySpecifiedException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
