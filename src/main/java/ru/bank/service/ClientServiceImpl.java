package ru.bank.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bank.dto.request.ClientRequestDto;
import ru.bank.dto.request.ContactDataRequestDto;
import ru.bank.dto.response.ClientFullResponseDto;
import ru.bank.dto.response.ClientShortResponseDto;
import ru.bank.entity.BankAccountEntity;
import ru.bank.entity.ClientEntity;
import ru.bank.enumeration.ContactDataStatus;
import ru.bank.enumeration.ContactDataType;
import ru.bank.exception.EntityDoesNotExistException;
import ru.bank.mapper.ClientMapper;
import ru.bank.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.bank.constants.Logger.*;
import static ru.bank.constants.Validator.*;

@Slf4j
@AllArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final BankAccountService bankAccountService;
    private final BankOperationService bankOperationService;
    private final ContactDataHistoryService contactDataHistoryService;
    private final JWTService jwtService;
    private final ValidatorService validatorService;

    @Transactional
    @Override
    public ClientFullResponseDto add(ClientRequestDto dto) {
        validatorService.validateAllFields(dto);
        boolean isLoginExists = clientRepository.existsByLogin(dto.getLogin());
        validatorService.validateLoginExistence(isLoginExists);

        List<String> phoneNumbersToValidation = new ArrayList<>(dto.getPhoneNumbers());
        List<String> phoneNumbersToDatabase = contactDataHistoryService.findByTypeAndStatus(
                ContactDataType.PHONE_NUMBER, ContactDataStatus.ADDED);
        phoneNumbersToValidation.retainAll(phoneNumbersToDatabase);
        validatorService.validateContactDataUniqueness(phoneNumbersToValidation, ERROR_MESSAGE_24);

        List<String> emailsToValidation = new ArrayList<>(dto.getEmails());
        List<String> emailsToDatabase = contactDataHistoryService.findByTypeAndStatus(
                ContactDataType.EMAIL, ContactDataStatus.ADDED);
        emailsToValidation.retainAll(emailsToDatabase);
        validatorService.validateContactDataUniqueness(emailsToValidation, ERROR_MESSAGE_24);

        ClientEntity client = clientMapper.dtoToEntity(dto);
        client = clientRepository.saveAndFlush(client);
        log.info(LOGGER_MESSAGE_3, client.getId());
        BankAccountEntity bankAccount = bankAccountService.add(client, dto.getBankAccountRequestDto());
        client.setBankAccount(bankAccount);
        client = clientRepository.saveAndFlush(client);
        contactDataHistoryService.add(dto.getLogin(), ContactDataType.PHONE_NUMBER, dto.getPhoneNumbers(),
                ContactDataStatus.ADDED);
        contactDataHistoryService.add(dto.getLogin(), ContactDataType.EMAIL, dto.getEmails(),
                ContactDataStatus.ADDED);
        ClientFullResponseDto clientFullResponseDto = clientMapper.entityToDto(client);
        clientFullResponseDto.setBankAccountShortResponseDto(bankAccountService.entityToDto(bankAccount));
        clientFullResponseDto.setJwt(jwtService.createJWT(dto));
        bankOperationService.firstReplenishmentAtRegistration(client, dto.getBankAccountRequestDto().getAmount());
        return clientFullResponseDto;
    }

    @Transactional
    @Override
    public ClientShortResponseDto addOwnContactData(ClientEntity client, ContactDataRequestDto dto) {
        validatorService.validateTwoCollectionsForEmptiness(dto.getPhoneNumbers(), dto.getEmails(), ERROR_MESSAGE_22);
        addPhoneNumbers(client, dto.getPhoneNumbers());
        addEmails(client, dto.getEmails());
        client = clientRepository.saveAndFlush(client);
        ClientFullResponseDto clientFullResponseDto = clientMapper.entityToDto(client);
        ClientShortResponseDto clientShortResponseDto = clientMapper.dtoToDto(clientFullResponseDto);
        log.info(LOGGER_MESSAGE_9, client.getId());
        return clientShortResponseDto;
    }

    private void addPhoneNumbers(ClientEntity client, List<String> phoneNumbersToRemove) {
        if (phoneNumbersToRemove != null && !phoneNumbersToRemove.isEmpty()) {
            List<String> phoneNumbersToValidation = new ArrayList<>(phoneNumbersToRemove);
            validatorService.validateContactDataByPattern(phoneNumbersToValidation, PHONE_NUMBER_PATTERN, ERROR_MESSAGE_7);
            List<String> phoneNumbersToDatabase = contactDataHistoryService.findByTypeAndStatus(
                    ContactDataType.PHONE_NUMBER, ContactDataStatus.ADDED);
            phoneNumbersToValidation.retainAll(phoneNumbersToDatabase);
            validatorService.validateContactDataUniqueness(phoneNumbersToValidation, ERROR_MESSAGE_24);
            List<String> clientPhoneNumbers = client.getPhoneNumbers();
            clientPhoneNumbers.addAll(phoneNumbersToRemove);
            client.setPhoneNumbers(clientPhoneNumbers);
            contactDataHistoryService.add(client.getLogin(), ContactDataType.PHONE_NUMBER, phoneNumbersToRemove,
                    ContactDataStatus.ADDED);
        }
    }

    private void addEmails(ClientEntity client, List<String> emailsToRemove) {
        if (emailsToRemove != null && !emailsToRemove.isEmpty()) {
            List<String> emailsToValidation = new ArrayList<>(emailsToRemove);
            validatorService.validateContactDataByPattern(emailsToValidation, EMAIL_PATTERN, ERROR_MESSAGE_8);
            List<String> emailsToDatabase = contactDataHistoryService.findByTypeAndStatus(
                    ContactDataType.EMAIL, ContactDataStatus.ADDED);
            emailsToValidation.retainAll(emailsToDatabase);
            validatorService.validateContactDataUniqueness(emailsToValidation, ERROR_MESSAGE_24);
            List<String> clientEmails = client.getEmails();
            clientEmails.addAll(emailsToRemove);
            client.setEmails(clientEmails);
            contactDataHistoryService.add(client.getLogin(), ContactDataType.EMAIL, emailsToRemove, ContactDataStatus.ADDED);
        }
    }

    @Transactional
    @Override
    public ClientEntity unlinkBankAccountFromClient(ClientEntity client) {
        client.setBankAccount(null);
        return clientRepository.saveAndFlush(client);
    }

    @Override
    public ClientEntity findByLogin(String login) {
        return clientRepository.findByLogin(login).orElseThrow(() -> new EntityDoesNotExistException(login, ClientEntity.class));
    }

    @Override
    public ClientEntity findById(int id) {
        return clientRepository.findById(id).orElseThrow(() -> new EntityDoesNotExistException(id, ClientEntity.class));
    }

    @Override
    public ClientEntity findByJWT() {
        String login = jwtService.findLoginFromJWT();
        return findByLogin(login);
    }

    @Transactional
    @Override
    public ClientShortResponseDto removeOwnContactData(ClientEntity client, ContactDataRequestDto dto) {
        validatorService.validateTwoCollectionsForEmptiness(dto.getPhoneNumbers(), dto.getEmails(), ERROR_MESSAGE_23);
        removePhoneNumbers(client, dto.getPhoneNumbers());
        removeEmails(client, dto.getEmails());
        client = clientRepository.saveAndFlush(client);
        ClientFullResponseDto clientFullResponseDto = clientMapper.entityToDto(client);
        ClientShortResponseDto clientShortResponseDto = clientMapper.dtoToDto(clientFullResponseDto);
        log.info(LOGGER_MESSAGE_10, client.getId());
        return clientShortResponseDto;
    }

    private void removePhoneNumbers(ClientEntity client, List<String> phoneNumbersToRemove) {
        if (phoneNumbersToRemove != null) {
            List<String> clientPhoneNumbers = client.getPhoneNumbers();
            validatorService.validateFilledContactData(clientPhoneNumbers, phoneNumbersToRemove);
            removeAndValidateContactData(clientPhoneNumbers, phoneNumbersToRemove, ERROR_MESSAGE_20);
            contactDataHistoryService.add(client.getLogin(), ContactDataType.PHONE_NUMBER, phoneNumbersToRemove,
                    ContactDataStatus.REMOVED);
        }
    }

    private void removeEmails(ClientEntity client, List<String> emailsToRemove) {
        if (emailsToRemove != null) {
            List<String> clientEmails = client.getEmails();
            validatorService.validateFilledContactData(clientEmails, emailsToRemove);
            removeAndValidateContactData(clientEmails, emailsToRemove, ERROR_MESSAGE_21);
            contactDataHistoryService.add(client.getLogin(), ContactDataType.EMAIL, emailsToRemove,
                    ContactDataStatus.REMOVED);
        }
    }

    private void removeAndValidateContactData(List<String> actualContactData, List<String> contactDataToProcess,
                                              String errorMessage) {
        actualContactData.removeAll(contactDataToProcess);
        validatorService.validateContactDataExist(actualContactData, errorMessage);
    }

    @Transactional
    @Override
    public void deleteAll() {
        clientRepository.deleteAll();
    }

}
