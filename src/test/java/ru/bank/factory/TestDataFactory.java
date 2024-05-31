package ru.bank.factory;

import ru.bank.dto.request.ClientRequestDto;

import static ru.bank.constants.Common.*;

public class TestDataFactory {

    public static ClientRequestDto createFirstClientRequestDto() {
        return new ClientRequestDto(LAST_NAME_1, FIRST_NAME_1, PATRONYMIC_1, BIRTH_DATE_1, PHONE_NUMBERS_1, EMAILS_1, LOGIN_1, PASSWORD_1,
                BANK_ACCOUNT_REQUEST_DTO_1, JWT_REQUEST_DTO_1);
    }

    public static ClientRequestDto createSecondClientRequestDto() {
        return new ClientRequestDto(LAST_NAME_2, FIRST_NAME_2, PATRONYMIC_2, BIRTH_DATE_2, PHONE_NUMBERS_2, EMAILS_2, LOGIN_2, PASSWORD_2,
                BANK_ACCOUNT_REQUEST_DTO_2, JWT_REQUEST_DTO_2);
    }

}