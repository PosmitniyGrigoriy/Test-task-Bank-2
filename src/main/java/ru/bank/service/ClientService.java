package ru.bank.service;

import ru.bank.dto.request.ClientRequestDto;
import ru.bank.dto.request.ContactDataRequestDto;
import ru.bank.dto.response.ClientFullResponseDto;
import ru.bank.dto.response.ClientShortResponseDto;
import ru.bank.entity.ClientEntity;

public interface ClientService {

    ClientFullResponseDto add(ClientRequestDto dto);

    ClientShortResponseDto addOwnContactData(ClientEntity client, ContactDataRequestDto dto);

    ClientEntity unlinkBankAccountFromClient(ClientEntity client);

    ClientEntity findByLogin(String login);

    ClientEntity findById(int id);

    ClientEntity findByJWT();

    ClientShortResponseDto removeOwnContactData(ClientEntity client, ContactDataRequestDto dto);

    void deleteAll();

}
