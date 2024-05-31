package ru.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.bank.base.BaseEntity;
import ru.bank.enumeration.BankOperationStatus;

@ToString
@Getter
@Setter
@Entity
@Table(name = "BANK_OPERATIONS")
public class BankOperationEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    private ClientEntity client;

    @Column(name = "amount", nullable = false, updatable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_operation_status", nullable = false, updatable = false)
    private BankOperationStatus bankOperationStatus;

    @Column(name = "client_old_balance", nullable = false, updatable = false)
    private long clientOldBalance;

    @Column(name = "client_new_balance", nullable = false, updatable = false)
    private long clientNewBalance;

    @ManyToOne
    @JoinColumn(name = "recipient_id", updatable = false)
    private ClientEntity recipient;

    @Column(name = "recipient_old_balance", updatable = false)
    private long recipientOldBalance;

    @Column(name = "recipient_new_balance", updatable = false)
    private long recipientNewBalance;

    public BankOperationEntity(ClientEntity client, long amount, BankOperationStatus bankOperationStatus,
                               long clientOldBalance, long clientNewBalance, ClientEntity recipient,
                               long recipientOldBalance, long recipientNewBalance) {
        this.client = client;
        this.amount = amount;
        this.bankOperationStatus = bankOperationStatus;
        this.clientOldBalance = clientOldBalance;
        this.clientNewBalance = clientNewBalance;
        this.recipient = recipient;
        this.recipientOldBalance = recipientOldBalance;
        this.recipientNewBalance = recipientNewBalance;
    }

    public BankOperationEntity(ClientEntity client, long amount, BankOperationStatus bankOperationStatus,
                               long clientOldBalance, long clientNewBalance) {
        this.client = client;
        this.amount = amount;
        this.bankOperationStatus = bankOperationStatus;
        this.clientOldBalance = clientOldBalance;
        this.clientNewBalance = clientNewBalance;
    }

    public BankOperationEntity() { }

}