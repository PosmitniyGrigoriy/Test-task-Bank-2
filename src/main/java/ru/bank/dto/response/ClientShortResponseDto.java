package ru.bank.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientShortResponseDto {
    private int id;
    private Date createdAt;
    private Date updatedAt;
    private String lastName;
    private String firstName;
    private String patronymic;
    private LocalDate birthDate;
    private List<String> phoneNumbers;
    private List<String> emails;
    private String login;
}