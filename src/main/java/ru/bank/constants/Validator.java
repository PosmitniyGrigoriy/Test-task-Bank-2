package ru.bank.constants;

public class Validator {

    public static final int ZERO = 0;
    public static final int SIX = 6;
    public static final int EIGHT = 8;
    public static final int THOUSAND = 1000;

    public static final int AGE = 18;

    public static final String PHONE_NUMBER_PATTERN = "\\+7-(9[0-9]{2})-(\\d{3})-(\\d{2})-(\\d{2})";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\\.ru$";

    public static final String ERROR_MESSAGE_1 = "Нужно написать фамилию.";
    public static final String ERROR_MESSAGE_2 = "Нужно написать имя.";
    public static final String ERROR_MESSAGE_3 = "Нужно написать отчество.";
    public static final String ERROR_MESSAGE_4 = "Нужно написать дату рождения в прошлом времени. Также возраст должен быть 18 лет или старше.";
    public static final String ERROR_MESSAGE_5 = "Нужно написать хотя бы 1 номер телефона.";
    public static final String ERROR_MESSAGE_6 = "Нужно написать хотя бы 1 электронную почту.";
    public static final String ERROR_MESSAGE_7 = "Номер телефона пишется в формате +7-900-000-00-00.";
    public static final String ERROR_MESSAGE_8 = "Электронная почта пишется в формате mail@mail.ru. Только в домене ru.";
    public static final String ERROR_MESSAGE_9 = "Указанный логин уже занят.";
    public static final String ERROR_MESSAGE_10 = "Нужно написать логин с длиной от 6 символов.";
    public static final String ERROR_MESSAGE_11 = "Нужно написать пароль с длиной от 8 символов.";
    public static final String ERROR_MESSAGE_12 = "Нельзя создать токен с ChronoUnit = NANOS, MICROS, MILLIS, SECONDS.";
    public static final String ERROR_MESSAGE_13 = "Время работы токена должна быть от 10 минут.";
    public static final String ERROR_MESSAGE_14 = "Сумма пополнения должна быть больше или равна 1000 рублей.";
    public static final String ERROR_MESSAGE_15 = "На счете недостаточно средств для выполнения снятия.";
    public static final String ERROR_MESSAGE_16 = "На счете недостаточно средств для выполнения перевода.";
    public static final String ERROR_MESSAGE_17 = "Клиент хотел перевести деньги другому клиенту, но выбрал неправильный статус операции.";
    public static final String ERROR_MESSAGE_18 = "Нельзя установить аргумент(ы) для увеличения баланса клиента, где значение будет меньше или равно 0.";
    public static final String ERROR_MESSAGE_19 = "Клиент хотел пополнить или снять деньги со своего банковского счета, но выбрал неправильный статус операции.";
    public static final String ERROR_MESSAGE_20 = "Нельзя удалить все номера телефонов. Хотя бы один должен остаться.";
    public static final String ERROR_MESSAGE_21 = "Нельзя удалить все электронные почты. Хотя бы одна должна остаться.";
    public static final String ERROR_MESSAGE_22 = "Чтобы добавить номер телефона и/или электронную почту нужно сначала указать.";
    public static final String ERROR_MESSAGE_23 = "Чтобы удалить номер телефона и/или электронную почту нужно сначала указать.";
    public static final String ERROR_MESSAGE_24 = "Указанные контактные данные уже используются другим клиентом банка. Проверьте правильность ваших данных. Если они верны, и вы хотите их указать, то обратитесь в техническую поддержку банка.";
    public static final String ERROR_MESSAGE_25 = "Неправильно заполнены контактные данные. Была попытка удалить то, что не указано в контактных данных.";

    private Validator() { }

}
