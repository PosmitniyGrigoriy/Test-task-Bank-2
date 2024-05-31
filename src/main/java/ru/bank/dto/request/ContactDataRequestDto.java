package ru.bank.dto.request;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactDataRequestDto {
    private List<String> phoneNumbers;
    private List<String> emails;
}
