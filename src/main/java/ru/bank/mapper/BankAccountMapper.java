package ru.bank.mapper;

import org.mapstruct.Mapper;
import ru.bank.dto.response.BankAccountShortResponseDto;
import ru.bank.entity.BankAccountEntity;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    BankAccountShortResponseDto entityToDto(BankAccountEntity entity);
}