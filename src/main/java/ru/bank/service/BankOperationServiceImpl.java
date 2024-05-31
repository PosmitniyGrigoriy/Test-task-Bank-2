package ru.bank.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.bank.dto.request.BankAccountRequestDto;
import ru.bank.dto.response.BankAccountShortResponseDto;
import ru.bank.dto.response.BankOperationResponseDto;
import ru.bank.dto.response.MoneyTransferResponseDto;
import ru.bank.entity.BankAccountEntity;
import ru.bank.entity.BankOperationEntity;
import ru.bank.entity.ClientEntity;
import ru.bank.enumeration.BankOperationStatus;
import ru.bank.mapper.BankOperationMapper;
import ru.bank.properties.ApplicationProperties;
import ru.bank.repository.BankOperationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static ru.bank.constants.Logger.*;
import static ru.bank.constants.Validator.*;
import static ru.bank.enumeration.BankOperationStatus.INCREASE_CLIENT_BALANCE_BY_PERCENTAGE;

@Slf4j
@Service
public class BankOperationServiceImpl implements BankOperationService {

    private final BankOperationRepository bankOperationRepository;
    private final BankOperationMapper bankOperationMapper;
    private final BankAccountService bankAccountService;
    private final ValidatorService validatorService;
    private final ApplicationProperties applicationProperties;
    private final Executor taskExecutor;

    @Autowired
    public BankOperationServiceImpl(BankOperationRepository bankOperationRepository,
                                    BankOperationMapper bankOperationMapper,
                                    @Lazy BankAccountService bankAccountService,
                                    ValidatorService validatorService,
                                    ApplicationProperties applicationProperties,
                                    Executor taskExecutor) {
        this.bankOperationRepository = bankOperationRepository;
        this.bankOperationMapper = bankOperationMapper;
        this.bankAccountService = bankAccountService;
        this.validatorService = validatorService;
        this.applicationProperties = applicationProperties;
        this.taskExecutor = taskExecutor;
    }

    @Transactional
    @Override
    public BankOperationResponseDto firstReplenishmentAtRegistration(ClientEntity client, long amount) {
        BankOperationEntity bankOperation = new BankOperationEntity(client, amount,
                BankOperationStatus.REPLENISHMENT, 0, amount);
        bankOperation = bankOperationRepository.saveAndFlush(bankOperation);
        log.info(LOGGER_MESSAGE_5, client.getId(), amount);
        return bankOperationMapper.entityToDto(bankOperation);
    }

    @Transactional
    @Override
    public BankOperationResponseDto replenishment(ClientEntity client, BankAccountRequestDto dto) {
        return doOperation(client, dto, BankOperationStatus.REPLENISHMENT);
    }

    @Transactional
    @Override
    public BankOperationResponseDto withdrawal(ClientEntity client, BankAccountRequestDto dto) {
        return doOperation(client, dto, BankOperationStatus.WITHDRAWAL);
    }

    private BankOperationResponseDto doOperation(ClientEntity client, BankAccountRequestDto dto,
                                                 BankOperationStatus bankOperationStatus) {
        validatorService.validateOperationStatusReplenishmentAndWithdrawal(bankOperationStatus, ERROR_MESSAGE_19);
        BankAccountShortResponseDto bankAccountShortResponseDto =
                bankAccountService.setNewBalance(client.getBankAccount(), dto.getAmount(), bankOperationStatus);
        BankOperationEntity newBankOperation = new BankOperationEntity(client, dto.getAmount(),
                bankOperationStatus, 0, bankAccountShortResponseDto.getAmount());
        if (bankOperationStatus.equals(BankOperationStatus.REPLENISHMENT)) {
            newBankOperation.setClientOldBalance(bankAccountShortResponseDto.getAmount() - dto.getAmount());
        }
        if (bankOperationStatus.equals(BankOperationStatus.WITHDRAWAL)) {
            newBankOperation.setClientOldBalance(bankAccountShortResponseDto.getAmount() + dto.getAmount());
        }
        newBankOperation = bankOperationRepository.saveAndFlush(newBankOperation);
        logBankOperation(client.getId(), dto.getAmount(), bankOperationStatus);
        return bankOperationMapper.entityToDto(newBankOperation);
    }

    @Transactional
    @Override
    public BankOperationResponseDto transfer(ClientEntity sender, ClientEntity recipient, BankAccountRequestDto dto) {
        return doOperation(sender, recipient, dto, BankOperationStatus.TRANSFER);
    }

    private BankOperationResponseDto doOperation(ClientEntity sender,
                                                 ClientEntity recipient,
                                                 BankAccountRequestDto dto,
                                                 BankOperationStatus bankOperationStatus) {
        validatorService.validateOperationStatusTransfer(bankOperationStatus, ERROR_MESSAGE_17);
        MoneyTransferResponseDto moneyTransferResponseDto = bankAccountService.setNewBalance(sender.getBankAccount(),
                recipient.getBankAccount(), dto.getAmount());
        BankOperationEntity newBankOperation = new BankOperationEntity(sender, dto.getAmount(), bankOperationStatus,
                moneyTransferResponseDto.getSenderOldBalance(),
                moneyTransferResponseDto.getSenderNewBalance(),
                    recipient, moneyTransferResponseDto.getRecipientOldBalance(),
                moneyTransferResponseDto.getRecipientNewBalance());
        newBankOperation = bankOperationRepository.saveAndFlush(newBankOperation);
        logBankOperation(sender.getId(), dto.getAmount(), bankOperationStatus);
        return bankOperationMapper.entityToDto(newBankOperation);
    }

    private void logBankOperation(int clientId, long amount, BankOperationStatus bankOperationStatus) {
        switch (bankOperationStatus) {
            case REPLENISHMENT -> log.info(LOGGER_MESSAGE_5, clientId, amount);
            case WITHDRAWAL -> log.info(LOGGER_MESSAGE_6, clientId, amount);
            case TRANSFER -> log.info(LOGGER_MESSAGE_7, clientId, amount);
        }
    }

    @Transactional
    @Override
    public void createSetBankOperations(List<BankAccountEntity> updatedBankAccountsBatch,
                                        Map<String, Long> bankAccountsOldAmounts) {
        int batchSize = applicationProperties.getBatchSize();
        for (int i = 0; i < updatedBankAccountsBatch.size(); i += batchSize) {
            int fromIndex = i;
            int toIndex = Math.min(i + batchSize, updatedBankAccountsBatch.size());
            List<BankAccountEntity> bankAccountsBatch = updatedBankAccountsBatch.subList(fromIndex, toIndex);
            taskExecutor.execute(() -> processBatch(bankAccountsBatch, bankAccountsOldAmounts));
        }
    }

    private void processBatch(List<BankAccountEntity> bankAccountsBatch, Map<String, Long> bankAccountsOldAmounts) {
        List<BankOperationEntity> bankOperations = new ArrayList<>();
        for (BankAccountEntity bankAccount : bankAccountsBatch) {
            long clientOldBalance =  bankAccountsOldAmounts.get(bankAccount.getClient().getLogin());
            long amount = bankAccount.getAmount() - clientOldBalance;
            BankOperationEntity bankOperation = new BankOperationEntity(bankAccount.getClient(), amount,
                    INCREASE_CLIENT_BALANCE_BY_PERCENTAGE, clientOldBalance, bankAccount.getAmount());
            bankOperations.add(bankOperation);
        }
        bankOperationRepository.saveAllAndFlush(bankOperations);
    }

    @Transactional
    @Override
    public void deleteAll() {
        bankOperationRepository.deleteAll();
    }

}
