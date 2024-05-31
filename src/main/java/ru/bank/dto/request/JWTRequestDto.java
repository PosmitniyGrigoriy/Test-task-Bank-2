package ru.bank.dto.request;

import lombok.*;

import java.time.temporal.ChronoUnit;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JWTRequestDto {
    private int timeValue;
    private ChronoUnit timeUnit;
}