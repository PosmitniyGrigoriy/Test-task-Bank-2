package ru.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bank.entity.BankOperationEntity;

public interface BankOperationRepository extends JpaRepository<BankOperationEntity, Integer> {
}
