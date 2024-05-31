package ru.bank.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bank.service.BankAccountService;

import static ru.bank.constants.Logger.*;

@Slf4j
@AllArgsConstructor
@Service
public class SchedulerServiceImpl implements SchedulerService {

    private final BankAccountService bankAccountService;

    @Scheduled(cron = "${spring.task.scheduler.upload-cron}")
    public void increaseClientBalancesByPercent() {
        log.info(LOGGER_MESSAGE_1);
        bankAccountService.increaseClientBalancesByPercent();
        log.info(LOGGER_MESSAGE_2);
    }

}