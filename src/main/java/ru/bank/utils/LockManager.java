package ru.bank.utils;

import org.springframework.stereotype.Component;
import ru.bank.entity.BankAccountEntity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LockManager {

    private final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    public Lock createLock(String login) {
        while (locks.containsKey(login)) {
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        return locks.computeIfAbsent(login, key -> new ReentrantLock());
    }

    public void createLocks(List<BankAccountEntity> bankAccounts) {
        bankAccounts.forEach(bankAccount -> {
            String login = bankAccount.getClient().getLogin();
            Lock lock = createLock(login);
            lock.lock();
        });
    }

    public ConcurrentHashMap<String, Lock> getLocksMap() {
        return locks;
    }

    public void removeLock(String login, Lock lock) {
        locks.remove(login, lock);
    }

    public void removeLocks(List<BankAccountEntity> bankAccounts) {
        bankAccounts.forEach(bankAccount -> {
            String login = bankAccount.getClient().getLogin();
            Lock lock = getLocksMap().get(login);
            lock.unlock();
            removeLock(login, lock);
        });
    }

}