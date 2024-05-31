package ru.bank.service;

import ru.bank.dto.request.ClientRequestDto;

public interface JWTService {

    String createJWT(ClientRequestDto dto);

    String findLoginFromJWT();

}
