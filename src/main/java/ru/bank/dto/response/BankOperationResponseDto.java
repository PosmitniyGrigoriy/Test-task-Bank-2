package ru.bank.dto.response;

import lombok.*;
import ru.bank.enumeration.BankOperationStatus;

import java.util.Date;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BankOperationResponseDto {
    private int id;
    private Date createdAt;
    private Date updatedAt;
    private long amount;
    private ClientShortResponseDto client;
    private BankOperationStatus bankOperationStatus;
    private long clientOldBalance;
    private long clientNewBalance;
    private ClientShortResponseDto recipient;
    private long recipientOldBalance;
    private long recipientNewBalance;
}
