package ru.bank.dto.response;

import lombok.*;

import java.util.Date;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BankAccountShortResponseDto {
    private int id;
    private Date createdAt;
    private Date updatedAt;
    private long amount;
}