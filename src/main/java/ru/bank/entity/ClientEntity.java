package ru.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.bank.base.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CLIENTS")
public class ClientEntity extends BaseEntity {

    @Column(name = "lastName", nullable = false, updatable = false)
    private String lastName;

    @Column(name = "firstName", nullable = false, updatable = false)
    private String firstName;

    @Column(name = "patronymic", nullable = false, updatable = false)
    private String patronymic;

    @Column(name = "birth_date", nullable = false, updatable = false)
    private LocalDate birthDate;

    @Column(name = "phone_numbers", nullable = false)
    private List<String> phoneNumbers;

    @Column(name = "emails", nullable = false)
    private List<String> emails;

    @Column(name = "login", nullable = false, updatable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id", unique = true)
    private BankAccountEntity bankAccount;

}