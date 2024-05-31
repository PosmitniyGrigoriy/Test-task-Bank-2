package ru.bank.service;

import ru.bank.dto.request.BankAccountRequestDto;
import ru.bank.dto.response.BankAccountShortResponseDto;
import ru.bank.dto.response.MoneyTransferResponseDto;
import ru.bank.entity.BankAccountEntity;
import ru.bank.entity.ClientEntity;
import ru.bank.enumeration.BankOperationStatus;

public interface BankAccountService {

    BankAccountEntity add(ClientEntity client, BankAccountRequestDto dto);

    BankAccountShortResponseDto setNewBalance(BankAccountEntity bankAccount,
                                              long amount,
                                              BankOperationStatus bankOperationStatus);

    MoneyTransferResponseDto setNewBalance(BankAccountEntity firstBankAccount,
                                           BankAccountEntity secondBankAccount,
                                           long amount);

    BankAccountEntity setIncreaseBalanceArgs(BankAccountEntity bankAccount,
                                             long bankAccountIncreasePercent,
                                             long decreasingPercent);

    void increaseClientBalancesByPercent();

    void deleteAll();

    BankAccountShortResponseDto entityToDto(BankAccountEntity bankAccount);

}
