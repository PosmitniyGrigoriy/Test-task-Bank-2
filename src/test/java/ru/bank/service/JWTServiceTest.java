package ru.bank.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import ru.bank.ApplicationTest;
import ru.bank.exception.AuthenticationIsNotViaJWTException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.bank.constants.Swagger.*;
import static ru.bank.factory.TestDataFactory.createFirstClientRequestDto;
import static ru.bank.constants.Common.*;

public class JWTServiceTest extends ApplicationTest {

    @Autowired
    private JWTService jwtService;

    @Test
    public void testCreateJWT() {
        String jwt = jwtService.createJWT(createFirstClientRequestDto());

        assertNotNull(jwt);
        assertTrue(JWT.length() == 3);
    }

    @Test
    public void testFindLoginFromJWT() {
        Jwt jwt = Mockito.mock(Jwt.class);
        when(jwt.getClaimAsString(CI)).thenReturn(CLIENT_INFORMATION);
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String login = jwtService.findLoginFromJWT();

        assertNotNull(login);
        assertNotEquals(EMPTY, login);
        assertEquals(LOGIN_1, login);
    }

    @Test
    public void testFindLoginFromJWTNoAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertThrows(AuthenticationIsNotViaJWTException.class, () -> jwtService.findLoginFromJWT());
    }

    @Test
    public void testFindLoginFromJWTWithUsernamePasswordAuthentication() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(LOGIN_1, PASSWORD_1);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThrows(AuthenticationIsNotViaJWTException.class, () -> jwtService.findLoginFromJWT());
    }

}
