package ru.bank.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientRequestDto {
    private String lastName;
    private String firstName;
    private String patronymic;
    private LocalDate birthDate;
    private List<String> phoneNumbers;
    private List<String> emails;
    private String login;
    private String password;
    private BankAccountRequestDto bankAccountRequestDto;
    private JWTRequestDto jwtRequestDto;
}