package ru.bank.service;

import ru.bank.entity.ContactDataHistoryEntity;
import ru.bank.enumeration.ContactDataStatus;
import ru.bank.enumeration.ContactDataType;

import java.util.List;

public interface ContactDataHistoryService {

    List<ContactDataHistoryEntity> add(String login,
                                       ContactDataType contactDataType,
                                       List<String> contactData,
                                       ContactDataStatus contactDataStatus);

    List<String> findByTypeAndStatus(ContactDataType contactDataType, ContactDataStatus contactDataStatus);

    void deleteAll();

}
