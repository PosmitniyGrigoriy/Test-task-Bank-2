package ru.bank.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

import static ru.bank.constants.Swagger.*;

@Configuration
@OpenAPIDefinition(info = @Info(title = TITLE, version = VERSION,
        contact = @Contact(name = DEVELOPER_NAME, email = DEVELOPER_EMAIL)),
        security = @SecurityRequirement(name = BEARER_TOKEN)
)
@SecurityScheme(name = BEARER_TOKEN, type = SecuritySchemeType.HTTP, scheme = SCHEME, bearerFormat = JWT)
public class OpenApiConfig {
}