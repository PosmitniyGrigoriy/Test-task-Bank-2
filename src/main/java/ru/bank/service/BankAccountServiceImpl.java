package ru.bank.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import ru.bank.utils.BankAccountsOldAmountManager;
import ru.bank.utils.LockManager;
import ru.bank.properties.ApplicationProperties;
import ru.bank.dto.request.BankAccountRequestDto;
import ru.bank.dto.response.BankAccountShortResponseDto;
import ru.bank.dto.response.MoneyTransferResponseDto;
import ru.bank.entity.BankAccountEntity;
import ru.bank.entity.ClientEntity;
import ru.bank.enumeration.BankOperationStatus;
import ru.bank.mapper.BankAccountMapper;
import ru.bank.repository.BankAccountRepository;

import java.util.List;
import java.util.concurrent.Executor;

import static ru.bank.constants.Logger.*;
import static ru.bank.constants.Validator.*;

@AllArgsConstructor
@Slf4j
@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final BankOperationService bankOperationService;
    private final ValidatorService validatorService;
    private final ApplicationProperties applicationProperties;
    private final BankAccountsOldAmountManager bankAccountsOldAmountManager;
    private final LockManager lockManager;
    private final Executor taskExecutor;


    @Transactional
    @Override
    public BankAccountEntity add(ClientEntity client, BankAccountRequestDto dto) {
        BankAccountEntity bankAccount = new BankAccountEntity(dto.getAmount(), client);
        bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
        log.info(LOGGER_MESSAGE_4, client.getId());
        return bankAccount;
    }

    @Transactional
    @Override
    public void increaseClientBalancesByPercent() {
        long percentageToIncreaseAmount = applicationProperties.getBankAccountAmountIncreasePercent();
        long maxPercent = applicationProperties.getBankAccountAmountIncreasePercentMax();
        validatorService.validateArgumentsIncreasingBalance(percentageToIncreaseAmount, maxPercent, ERROR_MESSAGE_18);
        long remainderModulo = maxPercent % percentageToIncreaseAmount;
        if (remainderModulo > 0) {
            maxPercent = maxPercent - remainderModulo;
        }
        List<BankAccountEntity> bankAccounts = bankAccountRepository.findByAmountAndDecreasingPercent();
        increaseBankBalancesInBatches(bankAccounts, percentageToIncreaseAmount, maxPercent);
    }

    private void increaseBankBalancesInBatches(List<BankAccountEntity> bankAccounts,
                                               long percentageToIncreaseAmount,
                                               long maxPercent) {
        int batchSize = applicationProperties.getBatchSize();
        for (int i = 0; i < bankAccounts.size(); i += batchSize) {
            int fromIndex = i;
            int toIndex = Math.min(i + batchSize, bankAccounts.size());
            List<BankAccountEntity> bankAccountsBatch = bankAccounts.subList(fromIndex, toIndex);
            bankAccountsBatch.forEach(bankAccount -> Hibernate.initialize(bankAccount.getClient()));
            taskExecutor.execute(() -> processBatch(bankAccountsBatch, percentageToIncreaseAmount, maxPercent));
        }
    }

    private void processBatch(List<BankAccountEntity> bankAccountsBatch, long percentageToIncreaseAmount, long maxPercent) {
        lockManager.createLocks(bankAccountsBatch);
        bankAccountsBatch.forEach(bankAccount -> {
            bankAccountsOldAmountManager.addOldAmount(bankAccount.getClient().getLogin(), bankAccount.getAmount());
            if (bankAccount.getDecreasingPercent() == null) {
                bankAccount.setBankAccountIncreasePercent(percentageToIncreaseAmount);
                bankAccount.setDecreasingPercent(maxPercent - percentageToIncreaseAmount);
            } else {
                bankAccount.setDecreasingPercent(bankAccount.getDecreasingPercent() -
                        bankAccount.getBankAccountIncreasePercent());
            }
            long newAmount = bankAccount.getAmount() + (bankAccount.getAmount() *
                    bankAccount.getBankAccountIncreasePercent() / 100);
            bankAccount.setAmount(newAmount);
        });
        try {
            List<BankAccountEntity> updatedBankAccountsBatch = bankAccountRepository.saveAllAndFlush(bankAccountsBatch);
            bankOperationService.createSetBankOperations(updatedBankAccountsBatch,
                    bankAccountsOldAmountManager.getBankAccountsOldAmounts());
        } finally {
            lockManager.removeLocks(bankAccountsBatch);
        }
    }

    @Transactional
    @Override
    public BankAccountShortResponseDto setNewBalance(BankAccountEntity bankAccount,
                                                     long amount,
                                                     BankOperationStatus bankOperationStatus) {
        long oldAmount = bankAccount.getAmount();
        switch (bankOperationStatus) {
            case REPLENISHMENT -> {
                validatorService.validateReplenishmentAmount(amount, ERROR_MESSAGE_14);
                bankAccount.setAmount(bankAccount.getAmount() + amount);
            }
            case WITHDRAWAL -> {
                validatorService.validateWithdrawalAmount(bankAccount.getAmount(), amount, ERROR_MESSAGE_15);
                bankAccount.setAmount(bankAccount.getAmount() - amount);
            }
        }
        bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
        log.info(LOGGER_MESSAGE_8, bankAccount.getId(), oldAmount, bankAccount.getAmount());
        return bankAccountMapper.entityToDto(bankAccount);
    }

    @Transactional
    @Override
    public MoneyTransferResponseDto setNewBalance(BankAccountEntity firstBankAccount,
                                                  BankAccountEntity secondBankAccount,
                                                  long amount) {
        validatorService.validateTransferAmount(firstBankAccount.getAmount(), amount, ERROR_MESSAGE_16);
        long currentBalanceOnFirstBankAccount = firstBankAccount.getAmount();
        long currentBalanceOnSecondBankAccount = secondBankAccount.getAmount();
        firstBankAccount.setAmount(currentBalanceOnFirstBankAccount - amount);
        secondBankAccount.setAmount(currentBalanceOnSecondBankAccount + amount);
        firstBankAccount = bankAccountRepository.saveAndFlush(firstBankAccount);
        secondBankAccount = bankAccountRepository.saveAndFlush(secondBankAccount);
        log.info(LOGGER_MESSAGE_8, firstBankAccount.getId(), currentBalanceOnFirstBankAccount, firstBankAccount.getAmount());
        log.info(LOGGER_MESSAGE_8, secondBankAccount.getId(), currentBalanceOnSecondBankAccount, secondBankAccount.getAmount());
        return new MoneyTransferResponseDto(firstBankAccount.getClient().getId(), currentBalanceOnFirstBankAccount,
                firstBankAccount.getAmount(), secondBankAccount.getClient().getId(), currentBalanceOnSecondBankAccount,
                secondBankAccount.getAmount());
    }

    @Transactional
    @Override
    public BankAccountEntity setIncreaseBalanceArgs(BankAccountEntity bankAccount,
                                                    long bankAccountIncreasePercent,
                                                    long decreasingPercent) {
        validatorService.validateArgumentsIncreasingBalance(bankAccountIncreasePercent, decreasingPercent, ERROR_MESSAGE_18);
        bankAccount.setBankAccountIncreasePercent(bankAccountIncreasePercent);
        bankAccount.setDecreasingPercent(decreasingPercent);
        return bankAccountRepository.saveAndFlush(bankAccount);
    }

    @Transactional
    @Override
    public void deleteAll() {
        bankAccountRepository.deleteAll();
    }

    @Override
    public BankAccountShortResponseDto entityToDto(BankAccountEntity bankAccount) {
        return bankAccountMapper.entityToDto(bankAccount);
    }

}
