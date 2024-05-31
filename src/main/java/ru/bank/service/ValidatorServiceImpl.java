package ru.bank.service;

import org.springframework.stereotype.Service;
import ru.bank.dto.request.ClientRequestDto;
import ru.bank.enumeration.BankOperationStatus;
import ru.bank.exception.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.bank.constants.Validator.*;

@Service
public class ValidatorServiceImpl implements ValidatorService {

    @Override
    public void validateLastName(String lastName, String errorMessage) {
        validateString(lastName, errorMessage);
    }

    @Override
    public void validateFirstName(String firstName, String errorMessage) {
        validateString(firstName, errorMessage);
    }

    @Override
    public void validatePatronymic(String patronymic, String errorMessage) {
        validateString(patronymic, errorMessage);
    }

    private void validateString(String value, String errorMessage) {
        if (value == null || value.length() == ZERO) {
            throw new FieldInPersonalDataIsIncorrectlyFilledInException(errorMessage);
        }
    }

    @Override
    public void validateBirthDate(LocalDate birthDate, String errorMessage) {
        LocalDate today = LocalDate.now();
        LocalDate adultAge = today.minusYears(AGE);
        if (birthDate.isAfter(adultAge)) {
            throw new FieldInPersonalDataIsIncorrectlyFilledInException(errorMessage);
        }
    }

    @Override
    public void validatePhoneNumbersAndEmails(List<String> phoneNumbers, List<String> emails) {
        validateContactDataExist(phoneNumbers, ERROR_MESSAGE_5);
        validateContactDataByPattern(phoneNumbers, PHONE_NUMBER_PATTERN, ERROR_MESSAGE_7);

        validateContactDataExist(emails, ERROR_MESSAGE_6);
        validateContactDataByPattern(emails, EMAIL_PATTERN, ERROR_MESSAGE_8);
    }

    @Override
    public void validateContactDataExist(List<String> contactData, String errorMessage) {
        if (contactData.isEmpty()) {
            throw new ContactDataEmptyException(errorMessage);
        }
    }

    @Override
    public void validateContactDataByPattern(List<String> contactData, String pattern, String errorMessage) {
        for (String element : contactData) {
            if (!element.matches(pattern)) {
                throw new InvalidContactDataException(errorMessage);
            }
        }
    }

    @Override
    public void validateContactDataUniqueness(List<String> contactData, String errorMessage) {
        if (!contactData.isEmpty()) {
            throw new ContactDataAlreadyInUseException(errorMessage, contactData);
        }
    }

    @Override
    public void validateFilledContactData(List<String> actualContactData, List<String> contactDataToRemove) {
        contactDataToRemove.forEach(contactValueToRemove -> {
            if (!actualContactData.contains(contactValueToRemove)) {
                throw new IncorrectContactDeletionDataException(ERROR_MESSAGE_25, contactValueToRemove);
            }
        });
    }

    @Override
    public void validateTwoCollectionsForEmptiness(List<String> firstCollection,
                                                   List<String> secondCollection,
                                                   String errorMessage) {
        if (firstCollection == null && secondCollection == null) {
            throw new MustBeObjectsInCollectionsException(errorMessage);
        }
    }

    @Override
    public void validateLogin(String login, String errorMessage) {
        if (login == null || login.length() < SIX) {
            throw new FieldInPersonalDataIsIncorrectlyFilledInException(errorMessage);
        }
    }

    @Override
    public void validateLoginExistence(boolean isLoginExists) {
        if (isLoginExists) {
            throw new LoginBusyException(ERROR_MESSAGE_9);
        }
    }

    @Override
    public void validatePassword(String password, String errorMessage) {
        if (password == null || password.length() < EIGHT) {
            throw new FieldInPersonalDataIsIncorrectlyFilledInException(errorMessage);
        }
    }

    @Override
    public void validateReplenishmentAmount(long amount, String errorMessage) {
        if (amount < THOUSAND) {
            throw new AmountIsSpecifiedIncorrectlyException(errorMessage);
        }
    }

    @Override
    public void validateWithdrawalAmount(long amountOnAccount, long amountToWithdraw, String errorMessage) {
        if (amountToWithdraw > amountOnAccount) {
            throw new AmountIsSpecifiedIncorrectlyException(errorMessage);
        }
    }

    @Override
    public void validateTransferAmount(long amountOnAccount, long amountToTransfer, String errorMessage) {
        if (amountToTransfer == 0 || amountToTransfer > amountOnAccount) {
            throw new AmountIsSpecifiedIncorrectlyException(errorMessage);
        }
    }

    @Override
    public void validateArgumentsIncreasingBalance(long bankAccountIncreasePercent,
                                                   long decreasingPercent,
                                                   String errorMessage) {
        if (bankAccountIncreasePercent <= 0 || decreasingPercent <= 0) {
            throw new InvalidBalanceIncreaseArgumentsException(errorMessage);
        }
    }

    @Override
    public void validateOperationStatusTransfer(BankOperationStatus bankOperationStatus, String errorMessage) {
        if (!bankOperationStatus.equals(BankOperationStatus.TRANSFER)) {
            throw new BankOperationStatusIsIncorrectlySpecifiedException(errorMessage);
        }
    }

    @Override
    public void validateOperationStatusReplenishmentAndWithdrawal(BankOperationStatus bankOperationStatus,
                                                                  String errorMessage) {
        if (bankOperationStatus.equals(BankOperationStatus.TRANSFER) ||
                bankOperationStatus.equals(BankOperationStatus.INCREASE_CLIENT_BALANCE_BY_PERCENTAGE)) {
            throw new BankOperationStatusIsIncorrectlySpecifiedException(errorMessage);
        }
    }

    @Override
    public void validateTokenExpiry(int timeValue, ChronoUnit timeUnit) {
        if (timeUnit.equals(ChronoUnit.NANOS) || timeUnit.equals(ChronoUnit.MICROS) ||
                timeUnit.equals(ChronoUnit.MILLIS) || timeUnit.equals(ChronoUnit.SECONDS)) {
            throw new TokenDurationIncorrectException(ERROR_MESSAGE_12);
        }
        if (timeUnit.equals(ChronoUnit.MINUTES) && timeValue < 10) {
            throw new TokenDurationIncorrectException(ERROR_MESSAGE_13);
        }
    }

    @Override
    public void validateAllFields(ClientRequestDto dto) {
        validateLastName(dto.getLastName(), ERROR_MESSAGE_1);
        validateFirstName(dto.getFirstName(), ERROR_MESSAGE_2);
        validatePatronymic(dto.getPatronymic(), ERROR_MESSAGE_3);
        validateBirthDate(dto.getBirthDate(), ERROR_MESSAGE_4);
        validatePhoneNumbersAndEmails(dto.getPhoneNumbers(), dto.getEmails());
        validateLogin(dto.getLogin(), ERROR_MESSAGE_10);
        validatePassword(dto.getPassword(), ERROR_MESSAGE_11);
        validateTokenExpiry(dto.getJwtRequestDto().getTimeValue(), dto.getJwtRequestDto().getTimeUnit());
        validateReplenishmentAmount(dto.getBankAccountRequestDto().getAmount(), ERROR_MESSAGE_14);
    }

}
