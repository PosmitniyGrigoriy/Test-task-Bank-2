package ru.bank.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.bank.dto.request.ClientRequestDto;
import ru.bank.exception.AuthenticationIsNotViaJWTException;

import java.time.Instant;
import java.util.List;

import static ru.bank.constants.Swagger.*;

@AllArgsConstructor
@Service
public class JWTServiceImpl implements JWTService {

    private final JwtEncoder encoder;

    public String createJWT(ClientRequestDto dto) {
        String clientInformation = dto.getLastName() + SPACE + dto.getFirstName() + SPACE + dto.getPatronymic() + COLON
                + dto.getBirthDate() + COLON + dto.getLogin();
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(dto.getJwtRequestDto().getTimeValue(), dto.getJwtRequestDto().getTimeUnit()))
                .subject(SUBJECT)
                .audience(List.of(AUDIENCE))
                .claim(SCOPE, ROLE_CLIENT)
                .claim(CI, clientInformation)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // value = Миронов Михаил Петрович:1991-09-10:login
    @Override
    public String findLoginFromJWT() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
            Jwt jwtToken = jwtAuthentication.getToken();
            String value = jwtToken.getClaimAsString(CI);
            value = value.substring(value.lastIndexOf(COLON) + 1);
            return value;
        } else {
            throw new AuthenticationIsNotViaJWTException();
        }
    }

}
