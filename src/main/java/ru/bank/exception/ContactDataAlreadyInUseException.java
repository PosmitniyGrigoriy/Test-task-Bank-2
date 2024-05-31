package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

import java.util.List;

public class ContactDataAlreadyInUseException extends BaseException {
    public ContactDataAlreadyInUseException(String errorMessage, List<String> contactData) {
        super(HttpStatus.CONFLICT, errorMessage + " Эти контакты заняты: " + contactData);
    }
}
