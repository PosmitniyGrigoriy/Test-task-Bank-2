package ru.bank.dto.request;

import lombok.*;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BankAccountRequestDto {
    private long amount;
}