package ru.bank.dto.request;

import lombok.*;
import ru.bank.enumeration.BankOperationStatus;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BankOperationRequestDto {
    private int clientId;
    private long amount;
    private BankOperationStatus bankOperationStatus;
    private int recipientId;
}