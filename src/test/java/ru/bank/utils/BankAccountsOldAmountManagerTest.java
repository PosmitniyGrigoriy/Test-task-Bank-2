package ru.bank.utils;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import ru.bank.ApplicationTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ru.bank.constants.Common.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankAccountsOldAmountManagerTest extends ApplicationTest {

    @Autowired
    private BankAccountsOldAmountManager bankAccountsOldAmountManager;

    private Map<String, Long> bankAccountsOldAmounts;

    @Order(1)
    @Test
    public void testAddOldAmount() {
        bankAccountsOldAmountManager.clearOldAmounts();
        bankAccountsOldAmountManager.addOldAmount(LOGIN_10, AMOUNT_1);
        bankAccountsOldAmountManager.addOldAmount(LOGIN_11, AMOUNT_2);
        bankAccountsOldAmountManager.addOldAmount(LOGIN_12, AMOUNT_3);

        bankAccountsOldAmounts = bankAccountsOldAmountManager.getBankAccountsOldAmounts();

        assertNotNull(bankAccountsOldAmounts);
        assertEquals(3, bankAccountsOldAmounts.size());
        assertEquals(AMOUNT_1, bankAccountsOldAmounts.get(LOGIN_10));
        assertEquals(AMOUNT_2, bankAccountsOldAmounts.get(LOGIN_11));
        assertEquals(AMOUNT_3, bankAccountsOldAmounts.get(LOGIN_12));
    }

    @Order(2)
    @Test
    public void testGetBankAccountsOldAmounts() {
        bankAccountsOldAmounts = bankAccountsOldAmountManager.getBankAccountsOldAmounts();

        assertNotNull(bankAccountsOldAmounts);
        assertEquals(3, bankAccountsOldAmounts.size());
        assertEquals(AMOUNT_1, bankAccountsOldAmounts.get(LOGIN_10));
        assertEquals(AMOUNT_2, bankAccountsOldAmounts.get(LOGIN_11));
        assertEquals(AMOUNT_3, bankAccountsOldAmounts.get(LOGIN_12));
    }

    @Order(3)
    @Test
    public void testRemoveOldAmount() {
        bankAccountsOldAmountManager.removeOldAmount(LOGIN_11);

        assertNotNull(bankAccountsOldAmountManager.getBankAccountsOldAmounts());
        assertEquals(2, bankAccountsOldAmountManager.getBankAccountsOldAmounts().size());
        assertEquals(AMOUNT_1, bankAccountsOldAmountManager.getBankAccountsOldAmounts().get(LOGIN_10));
        assertEquals(AMOUNT_3, bankAccountsOldAmountManager.getBankAccountsOldAmounts().get(LOGIN_12));
    }

    @Order(4)
    @Test
    public void testClearOldAmounts() {
        bankAccountsOldAmountManager.clearOldAmounts();

        assertEquals(0, bankAccountsOldAmountManager.getBankAccountsOldAmounts().size());
    }

}
