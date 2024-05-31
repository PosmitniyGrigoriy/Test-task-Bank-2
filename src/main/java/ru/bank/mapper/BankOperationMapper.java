package ru.bank.mapper;

import org.mapstruct.Mapper;
import ru.bank.dto.response.BankOperationResponseDto;
import ru.bank.entity.BankOperationEntity;

@Mapper(componentModel = "spring")
public interface BankOperationMapper {
    BankOperationResponseDto entityToDto(BankOperationEntity entity);
}