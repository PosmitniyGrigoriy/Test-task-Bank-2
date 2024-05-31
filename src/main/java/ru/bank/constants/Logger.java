package ru.bank.constants;

public class Logger {

    public static final String LOGGER_MESSAGE_1 = "Запущен планировщик для увеличения балансов клиентов.";
    public static final String LOGGER_MESSAGE_2 = "Планировщик увеличил балансы клиентов и выключился.";
    public static final String LOGGER_MESSAGE_3 = "Добавлен клиент с id {}.";
    public static final String LOGGER_MESSAGE_4 = "Для клиента с id {} был создан банковский аккаунт.";
    public static final String LOGGER_MESSAGE_5 = "Клиент с id {} сделал пополнение своего банковского счета на сумму {}.";
    public static final String LOGGER_MESSAGE_6 = "Клиент с id {} снял деньги со своего банковского счета на сумму {}.";
    public static final String LOGGER_MESSAGE_7 = "Клиент с id {} перевел деньги со своего банковского счета " +
            "на банковский счет другого клиента этого банка. Сумма: {}.";
    public static final String LOGGER_MESSAGE_8 = "На банковском счете с id {} изменился баланс с {} на {}.";
    public static final String LOGGER_MESSAGE_9 = "Клиент с id {} добавил новую контактную информацию.";
    public static final String LOGGER_MESSAGE_10 = "Клиент с id {} удалил что-то из своей контактной информации.";

    private Logger() { }

}
