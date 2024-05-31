package ru.bank.utils;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import ru.bank.ApplicationTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LockManagerTest extends ApplicationTest {

    @Autowired
    private LockManager lockManager;

    private List<Lock> locks = new ArrayList<>();

    @Order(1)
    @Test
    public void testCreateLock() {
        for (int i = 0; i < 5; i++) {
            Lock lock = lockManager.createLock("login" + i);
            assertNotNull(lock);
            assertTrue(lock instanceof ReentrantLock, "Lock должен быть экземпляром ReentrantLock.");
            locks.add(lock);
        }
        assertEquals(5, lockManager.getLocksMap().size());
    }

    @Order(2)
    @Test
    public void testRemoveLock() {
        lockManager.removeLock("login0", lockManager.getLocksMap().get("login0"));
        lockManager.removeLock("login1", lockManager.getLocksMap().get("login1"));
        assertNull(lockManager.getLocksMap().get("login0"), "Lock должен быть удален с ConcurrentHashMap.");
        assertNull(lockManager.getLocksMap().get("login1"), "Lock должен быть удален с ConcurrentHashMap.");
        assertEquals(3, lockManager.getLocksMap().size());
    }

}
