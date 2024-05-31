package ru.bank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank.utils.LockManager;
import ru.bank.dto.request.BankAccountRequestDto;
import ru.bank.dto.response.BankOperationResponseDto;
import ru.bank.entity.ClientEntity;
import ru.bank.service.BankOperationService;
import ru.bank.service.ClientService;

import java.util.concurrent.locks.Lock;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/bank_operation")
@Tag(name = "Банковские операции", description = "Пополнить, снять, перевести")
public class BankOperationController {

    private final BankOperationService bankOperationService;
    private final ClientService clientService;
    private final LockManager lockManager;

    @Operation(summary = "Пополнить свой банковский счет")
    @PostMapping("/replenishment")
    public ResponseEntity<BankOperationResponseDto> replenishment(@RequestBody BankAccountRequestDto dto) {
        ClientEntity client = clientService.findByJWT();
        Lock lock = lockManager.createLock(client.getLogin());
        try {
            lock.lock();
            BankOperationResponseDto responseDto = bankOperationService.replenishment(client, dto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } finally {
            lock.unlock();
            lockManager.removeLock(client.getLogin(), lock);
        }
    }

    @Operation(summary = "Снять деньги со своего банковского счета")
    @PostMapping("/withdrawal")
    public ResponseEntity<BankOperationResponseDto> withdrawal(@RequestBody BankAccountRequestDto dto) {
        ClientEntity client = clientService.findByJWT();
        Lock lock = lockManager.createLock(client.getLogin());
        try {
            lock.lock();
            BankOperationResponseDto responseDto = bankOperationService.withdrawal(client, dto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } finally {
            lock.unlock();
            lockManager.removeLock(client.getLogin(), lock);
        }
    }

    @Operation(summary = "Перевести деньги со своего банковского счета на банковский счет другого клиента этого банка")
    @PostMapping("/transfer/{recipient_id}")
    public ResponseEntity<BankOperationResponseDto> transfer(@RequestBody BankAccountRequestDto dto,
                                                             @PathVariable int recipientId) {
        ClientEntity sender = clientService.findByJWT();
        ClientEntity recipient = clientService.findById(recipientId);
        Lock fromLock = lockManager.createLock(sender.getLogin());
        Lock toLock = lockManager.createLock(recipient.getLogin());
        // Определяем порядок блокировок для предотвращения взаимной блокировки
        Lock firstLock, secondLock;
        if (sender.getLogin().compareTo(recipient.getLogin()) < 0) {
            firstLock = fromLock;
            secondLock = toLock;
        } else {
            firstLock = toLock;
            secondLock = fromLock;
        }
        firstLock.lock();
        secondLock.lock();
        try {
            BankOperationResponseDto responseDto = bankOperationService.transfer(sender, recipient, dto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } finally {
            secondLock.unlock();
            firstLock.unlock();
            lockManager.removeLock(recipient.getLogin(), toLock);
            lockManager.removeLock(sender.getLogin(), fromLock);
        }
    }

}