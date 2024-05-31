package ru.bank.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bank.entity.ContactDataHistoryEntity;
import ru.bank.enumeration.ContactDataStatus;
import ru.bank.enumeration.ContactDataType;
import ru.bank.repository.ContactDataHistoryRepository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ContactDataHistoryServiceImpl implements ContactDataHistoryService {

    private final ContactDataHistoryRepository contactDataHistoryRepository;

    @Transactional
    @Override
    public List<ContactDataHistoryEntity> add(String login,
                                              ContactDataType contactDataType,
                                              List<String> contactData,
                                              ContactDataStatus contactDataStatus) {
        List<ContactDataHistoryEntity> contactDataHistoryEntities = new ArrayList<>();
        contactData.forEach(element -> contactDataHistoryEntities.add(
                new ContactDataHistoryEntity(login, contactDataType, element, contactDataStatus)));
        return contactDataHistoryRepository.saveAllAndFlush(contactDataHistoryEntities);
    }

    @Override
    public List<String> findByTypeAndStatus(ContactDataType contactDataType, ContactDataStatus contactDataStatus) {
        return contactDataHistoryRepository.findByTypeAndStatus(contactDataType, contactDataStatus);
    }

    @Transactional
    @Override
    public void deleteAll() {
        contactDataHistoryRepository.deleteAll();
    }

}
