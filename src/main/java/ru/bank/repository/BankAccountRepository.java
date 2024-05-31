package ru.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bank.entity.BankAccountEntity;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Integer> {
    @Query(value = "SELECT * " +
                   "FROM BANK_ACCOUNTS " +
                   "WHERE amount > 0 AND (decreasing_percent IS NULL OR decreasing_percent > 0)", nativeQuery = true)
    List<BankAccountEntity> findByAmountAndDecreasingPercent();
}
