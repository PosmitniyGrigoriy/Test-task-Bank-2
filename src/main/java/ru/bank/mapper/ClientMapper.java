package ru.bank.mapper;

import org.mapstruct.Mapper;
import ru.bank.dto.request.ClientRequestDto;
import ru.bank.dto.response.ClientFullResponseDto;
import ru.bank.dto.response.ClientShortResponseDto;
import ru.bank.entity.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientEntity dtoToEntity(ClientRequestDto dto);
    ClientFullResponseDto entityToDto(ClientEntity entity);
    ClientShortResponseDto dtoToDto(ClientFullResponseDto dto);
}