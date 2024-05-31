package ru.bank.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BankAccountsOldAmountManager {

    private final Map<String, Long> bankAccountsOldAmounts = new HashMap<>();

    public void addOldAmount(String login, long oldAmount) {
        bankAccountsOldAmounts.put(login, oldAmount);
    }

    public Map<String, Long> getBankAccountsOldAmounts() {
        return bankAccountsOldAmounts;
    }

    public void removeOldAmount(String login) {
        bankAccountsOldAmounts.remove(login);
    }

    public void clearOldAmounts() {
        bankAccountsOldAmounts.clear();
    }

}
