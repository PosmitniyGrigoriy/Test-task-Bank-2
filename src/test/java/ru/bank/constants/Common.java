package ru.bank.constants;

import ru.bank.dto.request.BankAccountRequestDto;
import ru.bank.dto.request.JWTRequestDto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Common {

    public static final String LAST_NAME_1 = "Морозов";
    public static final String FIRST_NAME_1 = "Виталий";
    public static final String PATRONYMIC_1 = "Максимович";
    public static final LocalDate BIRTH_DATE_1 = LocalDate.of(1990, 12, 24);
    public static final List<String> PHONE_NUMBERS_1 = List.of("+7-999-999-11-11", "+7-999-999-22-22");
    public static final List<String> EMAILS_1 = List.of("morozov_vm@rambler.ru", "morozov_vm@mail.ru", "morozov_vm@yandex.ru");
    public static final String LOGIN_1 = "morozov_vm";
    public static final String PASSWORD_1 = "password";
    public static final BankAccountRequestDto BANK_ACCOUNT_REQUEST_DTO_1 = new BankAccountRequestDto(300_000);
    public static final JWTRequestDto JWT_REQUEST_DTO_1 = new JWTRequestDto(1, ChronoUnit.DAYS);

    public static final String LAST_NAME_2 = "Зотов";
    public static final String FIRST_NAME_2 = "Олег";
    public static final String PATRONYMIC_2 = "Владимирович";
    public static final LocalDate BIRTH_DATE_2 = LocalDate.of(1992, 11, 18);
    public static final List<String> PHONE_NUMBERS_2 = List.of("+7-999-888-88-77", "+7-999-888-88-88");
    public static final List<String> EMAILS_2 = List.of("zotov_ov@rambler.ru", "zotov_ov@mail.ru", "zotov_ov@yandex.ru");
    public static final String LOGIN_2 = "zotov_ov";
    public static final String PASSWORD_2 = "password";
    public static final BankAccountRequestDto BANK_ACCOUNT_REQUEST_DTO_2 = new BankAccountRequestDto(1_000_000);
    public static final JWTRequestDto JWT_REQUEST_DTO_2 = new JWTRequestDto(1, ChronoUnit.DAYS);

    public static final long AMOUNT_1 = 300_000;
    public static final long AMOUNT_2 = 200_000;
    public static final long AMOUNT_3 = 500_000;
    public static final long AMOUNT_4 = 50_000;
    public static final long AMOUNT_5 = 450_000;
    public static final long AMOUNT_6 = 900;
    public static final long AMOUNT_7 = 10_000_000;
    public static final long AMOUNT_8 = 1_000_000;
    public static final long AMOUNT_9 = 2_449_999;
    public static final long AMOUNT_10 = 3_126_885;
    public static final long AMOUNT_11 = 231_526;

    public static final String LOGIN_10 = "login10";
    public static final String LOGIN_11 = "login11";
    public static final String LOGIN_12 = "login12";

    public static final String CLIENT_INFORMATION = "Морозов Виталий Максимович:1990-12-24:morozov_vm";

    public static final String EMPTY = "";

    private Common() { }

}
