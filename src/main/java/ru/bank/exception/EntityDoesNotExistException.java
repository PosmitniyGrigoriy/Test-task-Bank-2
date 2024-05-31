package ru.bank.exception;

import org.springframework.http.HttpStatus;
import ru.bank.base.BaseException;

public class EntityDoesNotExistException extends BaseException {
    public EntityDoesNotExistException(String login, Class<?> clazz) {
        super(HttpStatus.NOT_FOUND, String.format("Не найден объект класса '%s', где login = '%s'", clazz, login));
    }

    public EntityDoesNotExistException(int id, Class<?> clazz) {
        super(HttpStatus.NOT_FOUND, String.format("Не найден объект класса '%s', где id = '%s'", clazz, id));
    }
}