package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class MustBeObjectsInCollectionsException extends BaseException {
    public MustBeObjectsInCollectionsException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
