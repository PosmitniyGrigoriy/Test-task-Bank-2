package ru.bank.dto.response;

import lombok.*;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoneyTransferResponseDto {
    private int senderId;
    private long senderOldBalance;
    private long senderNewBalance;
    private int recipientId;
    private long recipientOldBalance;
    private long recipientNewBalance;
}