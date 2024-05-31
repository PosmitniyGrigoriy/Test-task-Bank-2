package ru.bank.service;

import ru.bank.dto.request.BankAccountRequestDto;
import ru.bank.dto.response.BankOperationResponseDto;
import ru.bank.entity.BankAccountEntity;
import ru.bank.entity.ClientEntity;

import java.util.List;
import java.util.Map;

public interface BankOperationService {

    BankOperationResponseDto firstReplenishmentAtRegistration(ClientEntity client, long amount);

    BankOperationResponseDto replenishment(ClientEntity client, BankAccountRequestDto dto);

    BankOperationResponseDto withdrawal(ClientEntity client, BankAccountRequestDto dto);

    BankOperationResponseDto transfer(ClientEntity sender, ClientEntity recipient, BankAccountRequestDto dto);

    void createSetBankOperations(List<BankAccountEntity> updatedBankAccountsBatch,
                                 Map<String, Long> bankAccountsOldAmounts);

    void deleteAll();

}
