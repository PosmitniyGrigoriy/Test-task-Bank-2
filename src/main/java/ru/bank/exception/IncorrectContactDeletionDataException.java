package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class IncorrectContactDeletionDataException extends BaseException {
    public IncorrectContactDeletionDataException(String errorMessage, String contactValue) {
        super(HttpStatus.CONFLICT, errorMessage + " Нужно поправить это: " + contactValue);
    }
}
