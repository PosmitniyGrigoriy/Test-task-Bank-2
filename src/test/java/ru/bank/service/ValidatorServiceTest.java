package ru.bank.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.bank.ApplicationTest;
import ru.bank.dto.request.ClientRequestDto;
import ru.bank.enumeration.BankOperationStatus;
import ru.bank.exception.*;
import ru.bank.factory.TestDataFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.bank.constants.Common.*;
import static ru.bank.constants.Validator.*;

public class ValidatorServiceTest extends ApplicationTest {

    @Autowired
    private ValidatorService validatorService;

    @Test
    public void testValidateLastNameNullValueThrowsException() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateLastName(null, ERROR_MESSAGE_1));
    }

    @Test
    public void testValidateLastNameEmptyValueThrowsException() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateLastName("", ERROR_MESSAGE_1));
    }

    @Test
    public void testValidateLastNameCorrectValueDoesNotThrowException() {
        assertDoesNotThrow(() -> validatorService.validateLastName(LAST_NAME_1, ERROR_MESSAGE_1));
    }

    @Test
    public void testValidateFirstNameNullValueThrowsException() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateFirstName(null, ERROR_MESSAGE_2));
    }

    @Test
    public void testValidateFirstNameEmptyValueThrowsException() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateFirstName("", ERROR_MESSAGE_2));
    }

    @Test
    public void testValidateFirstNameCorrectValueDoesNotThrowException() {
        assertDoesNotThrow(() -> validatorService.validateFirstName(FIRST_NAME_1, ERROR_MESSAGE_2));
    }

    @Test
    public void testValidatePatronymicNullValueThrowsException() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validatePatronymic(null, ERROR_MESSAGE_3));
    }

    @Test
    public void testValidatePatronymicEmptyValueThrowsException() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validatePatronymic("", ERROR_MESSAGE_3));
    }

    @Test
    public void testValidatePatronymicCorrectValueDoesNotThrowException() {
        assertDoesNotThrow(() -> validatorService.validatePatronymic(PATRONYMIC_1, ERROR_MESSAGE_3));
    }

    @Test
    public void testValidateBirthDateTodayThrowsException() {
        LocalDate today = LocalDate.now();

        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateBirthDate(today, ERROR_MESSAGE_4));
    }

    @Test
    public void testValidateBirthDateFutureDateThrowsException() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateBirthDate(futureDate, ERROR_MESSAGE_4));
    }

    @Test
    public void testValidateBirthDateLessThan18YearsAgoThrowsException() {
        LocalDate lessThan18YearsAgo = LocalDate.now().minusYears(AGE).plusDays(1);

        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateBirthDate(lessThan18YearsAgo, ERROR_MESSAGE_4));
    }

    @Test
    public void testValidateBirthDateMoreThan18YearsAgoDoesNotThrowException() {
        LocalDate moreThan18YearsAgo = LocalDate.now().minusYears(AGE).minusDays(1);

        assertDoesNotThrow(() -> validatorService.validateBirthDate(moreThan18YearsAgo, ERROR_MESSAGE_4));
    }

    @Test
    public void testValidatePhoneNumbersAndEmailsValidData() {
        assertDoesNotThrow(() -> validatorService.validatePhoneNumbersAndEmails(PHONE_NUMBERS_1, EMAILS_1));
    }

    @Test
    public void testValidatePhoneNumbersAndEmailsEmptyLists() {
        assertThrows(ContactDataEmptyException.class,
                () -> validatorService.validatePhoneNumbersAndEmails(List.of(), List.of()));
    }

    @Test
    public void testValidatePhoneNumbersAndEmailsValidPhonesEmptyEmails() {
        List<String> phoneNumbers = List.of("+7-999-999-11-11", "+7-999-999-22-22");

        assertThrows(ContactDataEmptyException.class,
                () -> validatorService.validatePhoneNumbersAndEmails(phoneNumbers, List.of()));
    }

    @Test
    public void testValidatePhoneNumbersAndEmailsEmptyPhonesValidEmails() {
        List<String> emails = List.of("test@rambler.ru", "test@mail.ru");

        assertThrows(ContactDataEmptyException.class,
                () -> validatorService.validatePhoneNumbersAndEmails(List.of(), emails));
    }

    @Test
    public void testValidatePhoneNumbersAndEmailsInvalidPhoneNumbers() {
        List<String> phoneNumbers = List.of("+79999991111", "+7-999-999-2222", "89999991111");

        assertThrows(InvalidContactDataException.class,
                () -> validatorService.validatePhoneNumbersAndEmails(phoneNumbers, List.of()));
    }

    @Test
    public void testValidatePhoneNumbersAndEmailsInvalidEmails() {
        List<String> phoneNumbers = List.of("+7-999-999-33-33", "+7-999-999-44-44");
        List<String> emails = List.of("test@rambler.com", "test@mail.net", "test@yandex.ru");

        assertThrows(InvalidContactDataException.class,
                () -> validatorService.validatePhoneNumbersAndEmails(phoneNumbers, emails));
    }

    @Test
    public void testValidateContactDataUniquenessEmptyCollection() {
        assertDoesNotThrow(() -> validatorService.validateContactDataUniqueness(List.of(), ERROR_MESSAGE_24));
    }

    @Test
    public void testValidateContactDataUniquenessNonEmptyCollection() {
        List<String> contactData = List.of("+7-999-999-11-11", "+7-999-999-222-22");

        assertThrows(ContactDataAlreadyInUseException.class,
                () -> validatorService.validateContactDataUniqueness(contactData, ERROR_MESSAGE_24));
    }

    @Test
    public void testValidateFilledContactDataAllToRemovePresent() {
        List<String> actualContactData = List.of("+7-999-999-11-11", "+7-999-999-22-22", "+7-999-999-33-33");
        List<String> contactDataToRemove = List.of("+7-999-999-11-11", "+7-999-999-22-22");

        assertDoesNotThrow(() -> validatorService.validateFilledContactData(actualContactData, contactDataToRemove));
    }

    @Test
    public void testValidateFilledContactDataSomeToRemoveNotPresent() {
        List<String> actualContactData = List.of("+7-999-999-11-11", "+7-999-999-22-22", "+7-999-999-33-33");
        List<String> contactDataToRemove = List.of("+7-999-999-44-44");

        assertThrows(IncorrectContactDeletionDataException.class,
                () -> validatorService.validateFilledContactData(actualContactData, contactDataToRemove));
    }

    @Test
    public void testValidateTwoCollectionsForEmptinessBothEmpty() {
        assertThrows(MustBeObjectsInCollectionsException.class,
                () -> validatorService.validateTwoCollectionsForEmptiness(null, null, ERROR_MESSAGE_22));
    }

    @Test
    public void testValidateTwoCollectionsForEmptinessBothFilled() {
        List<String> firstCollection = List.of("+7-999-999-11-11", "+7-999-999-22-22");
        List<String> secondCollection = List.of("+7-999-999-33-33", "+7-999-999-44-44");

        assertDoesNotThrow(() -> validatorService.validateTwoCollectionsForEmptiness(firstCollection, secondCollection, ERROR_MESSAGE_22));
    }

    @Test
    public void testValidateLoginNull() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateLogin(null, ERROR_MESSAGE_10));
    }

    @Test
    public void testValidateLoginTooShort() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateLogin("short", ERROR_MESSAGE_10));
    }

    @Test
    public void testValidateLoginValid() {
        assertDoesNotThrow(() -> validatorService.validateLogin("validLogin", ERROR_MESSAGE_10));
    }

    @Test
    public void testValidateLoginExistenceLoginExists() {
        assertThrows(LoginBusyException.class, () -> validatorService.validateLoginExistence(true));
    }

    @Test
    public void testValidateLoginExistenceLoginDoesNotExist() {
        assertDoesNotThrow(() -> validatorService.validateLoginExistence(false));
    }

    @Test
    public void testValidatePasswordNull() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validatePassword(null, ERROR_MESSAGE_11));
    }

    @Test
    public void testValidatePasswordTooShort() {
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validatePassword("short", ERROR_MESSAGE_11));
    }

    @Test
    public void testValidatePasswordValid() {
        assertDoesNotThrow(() -> validatorService.validatePassword("validPassword", ERROR_MESSAGE_11));
    }

    @Test
    public void testValidateReplenishmentAmountValidAmount() {
        assertDoesNotThrow(() -> validatorService.validateReplenishmentAmount(THOUSAND, ERROR_MESSAGE_14));
    }

    @Test
    public void testValidateReplenishmentAmountTooLowAmount() {
        assertThrows(AmountIsSpecifiedIncorrectlyException.class,
                () -> validatorService.validateReplenishmentAmount(THOUSAND - 1, ERROR_MESSAGE_14));
    }

    @Test
    public void testValidateWithdrawalAmountValidAmount() {
        assertDoesNotThrow(() -> validatorService.validateWithdrawalAmount(THOUSAND, THOUSAND, ERROR_MESSAGE_15));
    }

    @Test
    public void testValidateWithdrawalAmountTooHighAmount() {
        assertThrows(AmountIsSpecifiedIncorrectlyException.class,
                () -> validatorService.validateWithdrawalAmount(THOUSAND, THOUSAND + 1, ERROR_MESSAGE_15));
    }

    @Test
    public void testValidateTransferAmountValidAmount() {
        assertDoesNotThrow(() -> validatorService.validateTransferAmount(THOUSAND, THOUSAND, ERROR_MESSAGE_16));
    }

    @Test
    public void testValidateTransferAmountTooHighAmount() {
        assertThrows(AmountIsSpecifiedIncorrectlyException.class,
                () -> validatorService.validateTransferAmount(THOUSAND, THOUSAND + 1, ERROR_MESSAGE_16));
    }

    @Test
    public void testValidateArgumentsBothZero() {
        assertThrows(InvalidBalanceIncreaseArgumentsException.class, () ->
                validatorService.validateArgumentsIncreasingBalance(0, 0,
                        ERROR_MESSAGE_18));
    }

    @Test
    public void testValidateArgumentsFirstZeroSecondOne() {
        assertThrows(InvalidBalanceIncreaseArgumentsException.class, () ->
                validatorService.validateArgumentsIncreasingBalance(0, 1,
                        ERROR_MESSAGE_18));
    }

    @Test
    public void testValidateArgumentsFirstOneSecondOne() {
        assertThrows(InvalidBalanceIncreaseArgumentsException.class, () ->
                validatorService.validateArgumentsIncreasingBalance(1, 0,
                ERROR_MESSAGE_18));
    }

    @Test
    public void testValidateArgumentsBothOne() {
        assertDoesNotThrow(() -> validatorService.validateArgumentsIncreasingBalance(1, 1,
                ERROR_MESSAGE_18));
    }

    @Test
    public void testValidateOperationStatusTransferCorrectStatus() {
        assertDoesNotThrow(() -> validatorService.validateOperationStatusTransfer(
                BankOperationStatus.TRANSFER, ERROR_MESSAGE_17));
    }

    @Test
    public void testValidateOperationStatusTransferIncorrectStatus() {
        assertThrows(BankOperationStatusIsIncorrectlySpecifiedException.class,
                () -> validatorService.validateOperationStatusTransfer(
                        BankOperationStatus.REPLENISHMENT, ERROR_MESSAGE_17));

        assertThrows(BankOperationStatusIsIncorrectlySpecifiedException.class,
                () -> validatorService.validateOperationStatusTransfer(
                        BankOperationStatus.WITHDRAWAL, ERROR_MESSAGE_17));

        assertThrows(BankOperationStatusIsIncorrectlySpecifiedException.class,
                () -> validatorService.validateOperationStatusTransfer(
                        BankOperationStatus.INCREASE_CLIENT_BALANCE_BY_PERCENTAGE, ERROR_MESSAGE_17));
    }

    @Test
    public void testValidateOperationStatusReplenishmentAndWithdrawalCorrectStatus() {
        assertDoesNotThrow(() -> validatorService.validateOperationStatusReplenishmentAndWithdrawal(
                BankOperationStatus.REPLENISHMENT, ERROR_MESSAGE_19));
        assertDoesNotThrow(() -> validatorService.validateOperationStatusReplenishmentAndWithdrawal(
                BankOperationStatus.WITHDRAWAL, ERROR_MESSAGE_19));
    }

    @Test
    public void testValidateOperationStatusReplenishmentAndWithdrawalIncorrectStatus() {
        assertThrows(BankOperationStatusIsIncorrectlySpecifiedException.class,
                () -> validatorService.validateOperationStatusReplenishmentAndWithdrawal(
                        BankOperationStatus.TRANSFER, ERROR_MESSAGE_19));

        assertThrows(BankOperationStatusIsIncorrectlySpecifiedException.class,
                () -> validatorService.validateOperationStatusReplenishmentAndWithdrawal(
                        BankOperationStatus.INCREASE_CLIENT_BALANCE_BY_PERCENTAGE, ERROR_MESSAGE_19));
    }

    @Test
    public void testValidateTokenExpiryTimeUnitNanos() {
        assertThrows(TokenDurationIncorrectException.class,
                () -> validatorService.validateTokenExpiry(1, ChronoUnit.NANOS));
    }

    @Test
    public void testValidateTokenExpiryTimeUnitMicros() {
        assertThrows(TokenDurationIncorrectException.class,
                () -> validatorService.validateTokenExpiry(1, ChronoUnit.MICROS));
    }

    @Test
    public void testValidateTokenExpiryTimeUnitMillis() {
        assertThrows(TokenDurationIncorrectException.class,
                () -> validatorService.validateTokenExpiry(1, ChronoUnit.MILLIS));
    }

    @Test
    public void testValidateTokenExpiryTimeUnitSeconds() {
        assertThrows(TokenDurationIncorrectException.class,
                () -> validatorService.validateTokenExpiry(1, ChronoUnit.SECONDS));
    }

    @Test
    public void testValidateTokenExpiryTimeUnitMinutesLessThan10() {
        assertThrows(TokenDurationIncorrectException.class,
                () -> validatorService.validateTokenExpiry(5, ChronoUnit.MINUTES));
    }

    @Test
    public void testValidateTokenExpirySuccessful() {
        assertDoesNotThrow(() -> validatorService.validateTokenExpiry(10, ChronoUnit.MINUTES));
    }

    @Test
    public void testValidateAllFieldsSuccessful() {
        ClientRequestDto dto = TestDataFactory.createFirstClientRequestDto();

        assertDoesNotThrow(() -> validatorService.validateAllFields(dto));
    }

    @Test
    public void testValidateAllFieldsFailureNullLastName() {
        ClientRequestDto dto = TestDataFactory.createFirstClientRequestDto();
        dto.setLastName(null);
        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class,
                () -> validatorService.validateAllFields(dto));
    }

}
