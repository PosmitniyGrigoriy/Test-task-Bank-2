package ru.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.bank.entity.ContactDataHistoryEntity;
import ru.bank.enumeration.ContactDataStatus;
import ru.bank.enumeration.ContactDataType;

import java.util.List;

public interface ContactDataHistoryRepository extends JpaRepository<ContactDataHistoryEntity, Integer> {

    @Query("SELECT c.contactValue " +
           "FROM ContactDataHistoryEntity c " +
           "WHERE c.contactDataType = :contactDataType AND c.contactDataStatus = :contactDataStatus")
    List<String> findByTypeAndStatus(@Param("contactDataType") ContactDataType contactDataType,
                                     @Param("contactDataStatus") ContactDataStatus contactDataStatus);

}
