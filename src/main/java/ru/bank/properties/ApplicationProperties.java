package ru.bank.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ApplicationProperties {
    @Value("${bank.account.amount.increase.percent}")
    private int bankAccountAmountIncreasePercent;

    @Value("${bank.account.amount.increase.percent.max}")
    private int bankAccountAmountIncreasePercentMax;

    @Value("${thread.batch.size}")
    private int batchSize;
}