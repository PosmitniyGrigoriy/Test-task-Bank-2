package ru.bank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank.dto.request.ClientRequestDto;
import ru.bank.dto.request.ContactDataRequestDto;
import ru.bank.dto.response.ClientFullResponseDto;
import ru.bank.dto.response.ClientShortResponseDto;
import ru.bank.entity.ClientEntity;
import ru.bank.service.ClientService;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Клиенты", description = "Добавить")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Добавить клиента")
    @PostMapping("/add")
    public ResponseEntity<ClientFullResponseDto> add(@RequestBody ClientRequestDto dto) {
        ClientFullResponseDto responseDto = clientService.add(dto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Добавить свои контактные данные")
    @PatchMapping("/add_own_contact_data")
    public ResponseEntity<ClientShortResponseDto> addOwnContactData(@RequestBody ContactDataRequestDto dto) {
        ClientEntity client = clientService.findByJWT();
        ClientShortResponseDto responseDto = clientService.addOwnContactData(client, dto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Удалить свои контактные данные")
    @PatchMapping("/remove_own_contact_data")
    public ResponseEntity<ClientShortResponseDto> removeOwnContactData(@RequestBody ContactDataRequestDto dto) {
        ClientEntity client = clientService.findByJWT();
        ClientShortResponseDto responseDto = clientService.removeOwnContactData(client, dto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
