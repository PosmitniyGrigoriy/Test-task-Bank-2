package ru.bank.service;

import ru.bank.dto.request.ClientRequestDto;
import ru.bank.enumeration.BankOperationStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public interface ValidatorService {

    void validateLastName(String lastName, String errorMessage);

    void validateFirstName(String firstName, String errorMessage);

    void validatePatronymic(String patronymic, String errorMessage);

    void validateBirthDate(LocalDate birthDate, String errorMessage);

    void validatePhoneNumbersAndEmails(List<String> phoneNumbers, List<String> emails);

    void validateContactDataExist(List<String> contactData, String errorMessage);

    void validateContactDataByPattern(List<String> collection, String pattern, String errorMessage);

    void validateContactDataUniqueness(List<String> contactData, String errorMessage);

    void validateFilledContactData(List<String> actualContactData, List<String> contactDataToRemove);

    void validateTwoCollectionsForEmptiness(List<String> firstCollection, List<String> secondCollection, String errorMessage);

    void validateLogin(String login, String errorMessage);

    void validateLoginExistence(boolean isLoginExists);

    void validatePassword(String password, String errorMessage);

    void validateReplenishmentAmount(long amount, String errorMessage);

    void validateWithdrawalAmount(long amountOnAccount, long amountToWithdraw, String errorMessage);

    void validateTransferAmount(long amountOnAccount, long amountToTransfer, String errorMessage);

    void validateArgumentsIncreasingBalance(long bankAccountIncreasePercent, long decreasingPercent, String errorMessage);

    void validateOperationStatusTransfer(BankOperationStatus bankOperationStatus, String errorMessage);

    void validateOperationStatusReplenishmentAndWithdrawal(BankOperationStatus bankOperationStatus, String errorMessage);

    void validateTokenExpiry(int timeValue, ChronoUnit timeUnit);

    void validateAllFields(ClientRequestDto dto);

}
