package ru.bank.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.bank.base.BaseEntity;
import ru.bank.enumeration.ContactDataStatus;
import ru.bank.enumeration.ContactDataType;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CONTACT_DATA_HISTORY")
public class ContactDataHistoryEntity extends BaseEntity {

    @Column(name = "login", nullable = false, updatable = false)
    private String login;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_data_type", nullable = false, updatable = false)
    private ContactDataType contactDataType;

    @Column(name = "contact_value", nullable = false, updatable = false)
    private String contactValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_data_status", nullable = false)
    private ContactDataStatus contactDataStatus;

}
