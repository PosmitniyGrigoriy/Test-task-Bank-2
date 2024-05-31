package ru.bank.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import ru.bank.ApplicationTest;
import ru.bank.dto.request.BankAccountRequestDto;
import ru.bank.dto.request.ClientRequestDto;
import ru.bank.dto.request.ContactDataRequestDto;
import ru.bank.dto.response.BankOperationResponseDto;
import ru.bank.dto.response.ClientFullResponseDto;
import ru.bank.dto.response.ClientShortResponseDto;
import ru.bank.entity.BankAccountEntity;
import ru.bank.entity.ClientEntity;
import ru.bank.enumeration.BankOperationStatus;
import ru.bank.exception.*;
import ru.bank.factory.TestDataFactory;
import ru.bank.mapper.ClientMapper;
import ru.bank.repository.ClientRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.bank.constants.Common.*;
import static ru.bank.constants.Swagger.CI;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankCoreServicesTest extends ApplicationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankOperationService bankOperationService;

    @Autowired
    private ContactDataHistoryService contactDataHistoryService;

    @Mock
    private JWTService jwtService;

    @Autowired
    private ValidatorService validatorService;

    @Order(1)
    @Test
    public void testAddFirstUserSuccessful() {
        ClientRequestDto clientRequestDto = TestDataFactory.createFirstClientRequestDto();

        try {
            ClientFullResponseDto clientFullResponseDto = clientService.add(clientRequestDto);

            assertNotNull(clientFullResponseDto);
            assertEquals(1, clientFullResponseDto.getId());
            assertEquals(LAST_NAME_1, clientFullResponseDto.getLastName());
            assertEquals(FIRST_NAME_1, clientFullResponseDto.getFirstName());
            assertEquals(PATRONYMIC_1, clientFullResponseDto.getPatronymic());
            assertEquals(BIRTH_DATE_1, clientFullResponseDto.getBirthDate());
            assertEquals(PHONE_NUMBERS_1, clientFullResponseDto.getPhoneNumbers());
            assertEquals(EMAILS_1, clientFullResponseDto.getEmails());
            assertEquals(LOGIN_1, clientFullResponseDto.getLogin());
            assertEquals(AMOUNT_1, clientFullResponseDto.getBankAccountShortResponseDto().getAmount());
        } catch (LoginBusyException ex) {
            assertThrows(LoginBusyException.class, () -> clientService.add(clientRequestDto));
        }
    }

    /**
     * Создан один неудачный тест для регистрации клиента, так как все возможные варианты неправильно заполненного
     * DTO уже проверены в других тестах (в классе ValidatorServiceTest).
     */
    @Order(2)
    @Test
    public void testAddFirstUserFailure() {
        ClientRequestDto clientRequestDto = TestDataFactory.createFirstClientRequestDto();
        clientRequestDto.setLastName(null);

        assertThrows(FieldInPersonalDataIsIncorrectlyFilledInException.class, () -> clientService.add(clientRequestDto));
    }

    @Order(3)
    @Test
    public void testAddOwnContactDataBothPhoneNumbersAndEmailsAdded() {
        ClientEntity client = clientService.findByLogin(LOGIN_1);
        ContactDataRequestDto dto = new ContactDataRequestDto();
        dto.setPhoneNumbers(List.of("+7-902-000-00-11", "+7-902-000-00-22"));
        dto.setEmails(List.of("test1@narod.ru", "test2@narod.ru"));

        try {
            ClientShortResponseDto result = clientService.addOwnContactData(client, dto);

            assertNotNull(result);
            assertEquals(4, result.getPhoneNumbers().size());
            assertEquals(5, result.getEmails().size());
        } catch (ContactDataAlreadyInUseException ex) {
            assertThrows(ContactDataAlreadyInUseException.class, () -> clientService.addOwnContactData(client, dto));
        }
    }

    @Order(4)
    @Test
    public void testAddOwnContactDataOnlyPhoneNumbersAdded() {
        ClientEntity client = clientService.findByLogin(LOGIN_1);
        ContactDataRequestDto dto = new ContactDataRequestDto();
        dto.setPhoneNumbers(List.of("+7-902-000-00-33"));

        try {
            ClientShortResponseDto result = clientService.addOwnContactData(client, dto);

            assertNotNull(result);
            assertEquals(5, result.getPhoneNumbers().size());
            assertEquals(5, result.getEmails().size());
        } catch (ContactDataAlreadyInUseException ex) {
            assertThrows(ContactDataAlreadyInUseException.class, () -> clientService.addOwnContactData(client, dto));
        }
    }

    @Order(5)
    @Test
    public void testAddOwnContactDataOnlyEmailsAdded() {
        ClientEntity client = clientService.findByLogin(LOGIN_1);
        ContactDataRequestDto dto = new ContactDataRequestDto();
        dto.setEmails(List.of("test3@narod.ru"));

        try {
            ClientShortResponseDto result = clientService.addOwnContactData(client, dto);

            assertNotNull(result);
            assertEquals(5, result.getPhoneNumbers().size());
            assertEquals(6, result.getEmails().size());
        } catch (ContactDataAlreadyInUseException ex) {
            assertThrows(ContactDataAlreadyInUseException.class, () -> clientService.addOwnContactData(client, dto));
        }
    }

    @Order(6)
    @Test
    public void testRemoveOwnContactDataBothPhoneNumbersAndEmails() {
        ClientEntity client = clientService.findByLogin(LOGIN_1);
        ContactDataRequestDto dto = new ContactDataRequestDto();
        dto.setPhoneNumbers(List.of("+7-902-000-00-22", "+7-902-000-00-33"));
        dto.setEmails(List.of("test2@narod.ru", "test3@narod.ru"));

        try {
        ClientShortResponseDto result = clientService.removeOwnContactData(client, dto);

            assertNotNull(result);
            assertEquals(3, result.getPhoneNumbers().size());
            assertEquals(4, result.getEmails().size());
        } catch (IncorrectContactDeletionDataException ex) {
            assertThrows(IncorrectContactDeletionDataException.class,
                    () -> clientService.removeOwnContactData(client, dto));
        }
    }

    @Order(7)
    @Test
    public void testRemoveOwnContactDataOnlyPhoneNumbers() {
        ClientEntity client = clientService.findByLogin(LOGIN_1);
        ContactDataRequestDto dto = new ContactDataRequestDto();
        dto.setPhoneNumbers(List.of("+7-902-000-00-11"));

        try {
            ClientShortResponseDto result = clientService.removeOwnContactData(client, dto);

            assertNotNull(result);
            assertEquals(2, result.getPhoneNumbers().size());
            assertEquals(4, result.getEmails().size());
        } catch (IncorrectContactDeletionDataException ex) {
            assertThrows(IncorrectContactDeletionDataException.class,
                    () -> clientService.removeOwnContactData(client, dto));
        }
    }

    @Order(8)
    @Test
    public void testRemoveOwnContactDataOnlyEmails() {
        ClientEntity client = clientService.findByLogin(LOGIN_1);
        ContactDataRequestDto dto = new ContactDataRequestDto();
        dto.setEmails(List.of("test1@narod.ru"));

        try {
            ClientShortResponseDto result = clientService.removeOwnContactData(client, dto);

            assertNotNull(result);
            assertEquals(2, result.getPhoneNumbers().size());
            assertEquals(3, result.getEmails().size());
        } catch (IncorrectContactDeletionDataException ex) {
            assertThrows(IncorrectContactDeletionDataException.class,
                    () -> clientService.removeOwnContactData(client, dto));
        }
    }

    @Order(9)
    @Test
    public void testFindByLoginSuccess() {
        ClientEntity client = clientService.findByLogin(LOGIN_1);

        assertNotNull(client);
        assertEquals(1, client.getId());
        assertEquals(LAST_NAME_1, client.getLastName());
        assertEquals(FIRST_NAME_1, client.getFirstName());
        assertEquals(PATRONYMIC_1, client.getPatronymic());
        assertEquals(BIRTH_DATE_1, client.getBirthDate());
        assertEquals(LOGIN_1, client.getLogin());
    }

    @Order(10)
    @Test
    public void testFindByLoginFailure() {
        assertThrows(EntityDoesNotExistException.class, () -> clientService.findByLogin("login2024"));
    }

    @Order(11)
    @Test
    public void testFindByIdSuccess() {
        ClientEntity client = clientService.findById(1);

        assertNotNull(client);
        assertEquals(1, client.getId());
        assertEquals(LAST_NAME_1, client.getLastName());
        assertEquals(FIRST_NAME_1, client.getFirstName());
        assertEquals(PATRONYMIC_1, client.getPatronymic());
        assertEquals(BIRTH_DATE_1, client.getBirthDate());
        assertEquals(LOGIN_1, client.getLogin());
    }

    @Order(12)
    @Test
    public void testFindByIdFailure() {
        assertThrows(EntityDoesNotExistException.class, () -> clientService.findById(999));
    }

    @Order(13)
    @Test
    public void testFindByJWTSuccess() {
        Jwt jwt = Mockito.mock(Jwt.class);
        when(jwt.getClaimAsString(CI)).thenReturn(CLIENT_INFORMATION);
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(jwtService.findLoginFromJWT()).thenReturn(LOGIN_1);

        ClientEntity client = clientService.findByJWT();

        assertNotNull(client);
        assertEquals(1, client.getId());
        assertEquals(LAST_NAME_1, client.getLastName());
        assertEquals(FIRST_NAME_1, client.getFirstName());
        assertEquals(PATRONYMIC_1, client.getPatronymic());
        assertEquals(BIRTH_DATE_1, client.getBirthDate());
        assertEquals(LOGIN_1, client.getLogin());
    }

    @Order(14)
    @Test
    public void testReplenishmentSuccess() {
        ClientEntity client = clientService.findById(1);
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(AMOUNT_2);

        BankOperationResponseDto bankOperationResponseDto =
                bankOperationService.replenishment(client, bankAccountRequestDto);

        assertNotNull(bankOperationResponseDto);
        assertEquals(1, bankOperationResponseDto.getClient().getId());
        assertEquals(AMOUNT_2, bankOperationResponseDto.getAmount());
        assertEquals(BankOperationStatus.REPLENISHMENT, bankOperationResponseDto.getBankOperationStatus());
        assertEquals(AMOUNT_3 - AMOUNT_2, bankOperationResponseDto.getClientOldBalance());
        assertEquals(AMOUNT_3, bankOperationResponseDto.getClientNewBalance());
    }

    @Order(15)
    @Test
    public void testReplenishmentFailure() {
        ClientEntity client = clientService.findById(1);
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(AMOUNT_6);

        assertThrows(AmountIsSpecifiedIncorrectlyException.class, () ->
                bankOperationService.replenishment(client, bankAccountRequestDto));
    }

    @Order(16)
    @Test
    public void testWithdrawalSuccess() {
        ClientEntity client = clientService.findById(1);
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(AMOUNT_4);

        BankOperationResponseDto bankOperationResponseDto =
                bankOperationService.withdrawal(client, bankAccountRequestDto);

        assertNotNull(bankOperationResponseDto);
        assertEquals(1, bankOperationResponseDto.getClient().getId());
        assertEquals(AMOUNT_4, bankOperationResponseDto.getAmount());
        assertEquals(BankOperationStatus.WITHDRAWAL, bankOperationResponseDto.getBankOperationStatus());
        assertEquals(AMOUNT_3, bankOperationResponseDto.getClientOldBalance());
        assertEquals(AMOUNT_5, bankOperationResponseDto.getClientNewBalance());
    }

    @Order(17)
    @Test
    public void testWithdrawalFailure() {
        ClientEntity client = clientService.findById(1);
        BankAccountRequestDto firstBankAccountRequestDto = new BankAccountRequestDto(AMOUNT_5 + 1);
        BankAccountRequestDto secondBankAccountRequestDto = new BankAccountRequestDto(AMOUNT_7);

        assertThrows(AmountIsSpecifiedIncorrectlyException.class,
                () -> bankOperationService.withdrawal(client, firstBankAccountRequestDto));
        assertThrows(AmountIsSpecifiedIncorrectlyException.class,
                () -> bankOperationService.withdrawal(client, secondBankAccountRequestDto));
    }

    @Order(18)
    @Test
    public void testAddSecondUserSuccessful() {
        ClientRequestDto clientRequestDto = TestDataFactory.createSecondClientRequestDto();

        try {
            ClientFullResponseDto clientFullResponseDto = clientService.add(clientRequestDto);

            assertNotNull(clientFullResponseDto);
            assertEquals(LAST_NAME_2, clientFullResponseDto.getLastName());
            assertEquals(FIRST_NAME_2, clientFullResponseDto.getFirstName());
            assertEquals(PATRONYMIC_2, clientFullResponseDto.getPatronymic());
            assertEquals(BIRTH_DATE_2, clientFullResponseDto.getBirthDate());
            assertEquals(PHONE_NUMBERS_2, clientFullResponseDto.getPhoneNumbers());
            assertEquals(EMAILS_2, clientFullResponseDto.getEmails());
            assertEquals(LOGIN_2, clientFullResponseDto.getLogin());
            assertEquals(AMOUNT_8, clientFullResponseDto.getBankAccountShortResponseDto().getAmount());
        } catch (LoginBusyException ex) {
            assertThrows(LoginBusyException.class, () -> clientService.add(clientRequestDto));
        }
    }

    @Order(19)
    @Test
    public void testTransferFirstUserExistsSecondUserDoesNot() {
        ClientEntity sender = clientService.findByLogin(LOGIN_2);

        assertThrows(EntityDoesNotExistException.class, () -> clientService.findById(3));
    }

    @Order(20)
    @Test
    public void testTransferBothUsersExistNoAmountProvided() {
        ClientEntity sender = clientService.findByLogin(LOGIN_2);
        ClientEntity recipient = clientService.findById(1);
        BankAccountRequestDto dto = new BankAccountRequestDto(0);

        assertThrows(AmountIsSpecifiedIncorrectlyException.class,
                () -> bankOperationService.transfer(sender, recipient, dto));
    }

    @Order(21)
    @Test
    public void testTransferBothUsersExistAmountExceedsBalance() {
        ClientEntity sender = clientService.findByLogin(LOGIN_2);
        ClientEntity recipient = clientService.findById(1);
        BankAccountRequestDto dto = new BankAccountRequestDto(AMOUNT_8 + 1);

        assertThrows(AmountIsSpecifiedIncorrectlyException.class,
                () -> bankOperationService.transfer(sender, recipient, dto));
    }

    @Order(22)
    @Test
    public void testTransferBothUsersExistAmountEqualsBalance() {
        ClientEntity sender = clientService.findByLogin(LOGIN_2);
        ClientEntity recipient = clientService.findById(1);
        BankAccountRequestDto dto = new BankAccountRequestDto(AMOUNT_8);

        BankOperationResponseDto bankOperationResponseDto = bankOperationService.transfer(sender, recipient, dto);
        assertEquals(AMOUNT_8, bankOperationResponseDto.getAmount());
        assertEquals(LOGIN_2, bankOperationResponseDto.getClient().getLogin());
        assertEquals(BankOperationStatus.TRANSFER, bankOperationResponseDto.getBankOperationStatus());
        assertEquals(AMOUNT_8, bankOperationResponseDto.getClientOldBalance());
        assertEquals(0, bankOperationResponseDto.getClientNewBalance());
        assertEquals(1, bankOperationResponseDto.getRecipient().getId());
        assertEquals(LOGIN_1, bankOperationResponseDto.getRecipient().getLogin());
        assertEquals(AMOUNT_5, bankOperationResponseDto.getRecipientOldBalance());
        assertEquals(AMOUNT_5 + AMOUNT_8, bankOperationResponseDto.getRecipientNewBalance());
    }

    @Order(23)
    @Test
    public void testSecondReplenishmentSuccess() {
        ClientEntity client = clientService.findByLogin(LOGIN_2);
        BankAccountRequestDto bankAccountRequestDto = new BankAccountRequestDto(AMOUNT_8);

        BankOperationResponseDto bankOperationResponseDto =
                bankOperationService.replenishment(client, bankAccountRequestDto);

        assertNotNull(bankOperationResponseDto);
        assertEquals(LOGIN_2, bankOperationResponseDto.getClient().getLogin());
        assertEquals(AMOUNT_8, bankOperationResponseDto.getAmount());
        assertEquals(BankOperationStatus.REPLENISHMENT, bankOperationResponseDto.getBankOperationStatus());
        assertEquals(0, bankOperationResponseDto.getClientOldBalance());
        assertEquals(AMOUNT_8, bankOperationResponseDto.getClientNewBalance());
    }

    @Order(24)
    @Test
    public void testTransferBothUsersExistAmountLessThanBalance() {
        ClientEntity sender = clientService.findByLogin(LOGIN_2);
        ClientEntity recipient = clientService.findById(1);
        BankAccountRequestDto dto = new BankAccountRequestDto(AMOUNT_8 - 1);

        BankOperationResponseDto bankOperationResponseDto = bankOperationService.transfer(sender, recipient, dto);
        assertEquals(AMOUNT_8 - 1, bankOperationResponseDto.getAmount());
        assertEquals(LOGIN_2, bankOperationResponseDto.getClient().getLogin());
        assertEquals(BankOperationStatus.TRANSFER, bankOperationResponseDto.getBankOperationStatus());
        assertEquals(AMOUNT_8, bankOperationResponseDto.getClientOldBalance());
        assertEquals(1, bankOperationResponseDto.getClientNewBalance());
        assertEquals(1, bankOperationResponseDto.getRecipient().getId());
        assertEquals(LOGIN_1, bankOperationResponseDto.getRecipient().getLogin());
        assertEquals(AMOUNT_5 + AMOUNT_8, bankOperationResponseDto.getRecipientOldBalance());
        assertEquals(AMOUNT_9, bankOperationResponseDto.getRecipientNewBalance());
    }

    /**
     * В приложении есть планировщик. Для обычной базы он работает 1 раз в минуту, а для тестов каждые 5 секунд.
     * Также для тестов сделал 27% насколько максимум может увеличиться стартовый баланс, увеличивая каждый раз на 5%.
     * Так как планировщик запускается сам при запуске приложения, то в тесте его вызывать не нужно.
     * Сначала проходят все тесты (кроме планировщика), а потом запускается планировщик
     * (через 5 секунд после старта приложения). Операций для увеличения баланса может быть максимум 5 (для тестов),
     * то сделал для тестов Thread.sleep(30000), чтобы все операции планировщика прошли успешно, и тесты были успешны.
     */
    @Order(25)
    @Test
    public void testIncreaseClientBalancesByPercent() {
        ClientEntity client = clientService.findByLogin(LOGIN_2);
        bankOperationService.replenishment(client, new BankAccountRequestDto(AMOUNT_2));
        bankAccountService.setIncreaseBalanceArgs(client.getBankAccount(), 5, 15);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        BankAccountEntity firstBankAccount = clientService.findByLogin(LOGIN_1).getBankAccount();
        BankAccountEntity secondBankAccount = clientService.findByLogin(LOGIN_2).getBankAccount();

        assertEquals(LOGIN_1, firstBankAccount.getClient().getLogin());
        assertEquals(5, firstBankAccount.getBankAccountIncreasePercent());
        assertEquals(0, firstBankAccount.getDecreasingPercent());
        assertEquals(AMOUNT_10, firstBankAccount.getAmount());

        assertEquals(LOGIN_2, secondBankAccount.getClient().getLogin());
        assertEquals(5, secondBankAccount.getBankAccountIncreasePercent());
        assertEquals(0, secondBankAccount.getDecreasingPercent());
        assertEquals(AMOUNT_11, secondBankAccount.getAmount());
    }

    @Order(26)
    @Test
    public void clearDatabase() {
        ClientEntity firstClient = clientService.findByLogin(LOGIN_1);
        ClientEntity secondClient = clientService.findByLogin(LOGIN_2);

        contactDataHistoryService.deleteAll();
        bankOperationService.deleteAll();
        clientService.unlinkBankAccountFromClient(firstClient);
        clientService.unlinkBankAccountFromClient(secondClient);
        bankAccountService.deleteAll();
        clientService.deleteAll();
    }

}
