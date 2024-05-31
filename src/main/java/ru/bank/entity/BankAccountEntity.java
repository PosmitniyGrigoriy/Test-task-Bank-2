package ru.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.bank.base.BaseEntity;

@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "BANK_ACCOUNTS")
public class BankAccountEntity extends BaseEntity {

    @Column(name = "amount", nullable = false)
    private long amount;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false, updatable = false, unique = true)
    private ClientEntity client;

    @Column(name = "bank_account_increase_percent")
    private Long bankAccountIncreasePercent;

    @Column(name = "decreasing_percent")
    private Long decreasingPercent;

    public BankAccountEntity(long amount, ClientEntity client) {
        this.amount = amount;
        this.client = client;
    }

}