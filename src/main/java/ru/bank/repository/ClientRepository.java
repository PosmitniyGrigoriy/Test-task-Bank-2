package ru.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.bank.entity.ClientEntity;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    Optional<ClientEntity> findByLogin(String login);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClientEntity c WHERE c.login = :login")
    boolean existsByLogin(@Param("login") String login);

}