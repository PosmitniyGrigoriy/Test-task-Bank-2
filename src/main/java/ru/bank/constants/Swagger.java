package ru.bank.constants;

public class Swagger {

    public static final String TITLE   = "API";
    public static final String VERSION = "1";

    public static final String DEVELOPER_NAME  = "Grigoriy";
    public static final String DEVELOPER_EMAIL = "pga.profile@gmail.com";

    public static final String BEARER_TOKEN = "bearerToken";
    public static final String SCHEME = "bearer";
    public static final String JWT = "JWT";

    public static final String[] PUBLIC_PATHS = {
            "/api/clients/add",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    public static final String CONTENT_SECURITY_POLICY = "script-src 'self'";
    public static final String AUTHORITY = "SCOPE_ROLE_CLIENT";
    public static final String SECURITY_MATCHER = "/api/test/**";

    public static final String ISSUER = "Grigoriy Posmitniy";
    public static final String SUBJECT = "pga.profile@gmail.com";
    public static final String AUDIENCE = "bank.ru";
    public static final String SCOPE = "scope";
    public static final String ROLE_CLIENT = "ROLE_CLIENT";
    public static final String CI = "ci";
    public static final String COLON = ":";
    public static final String SPACE = " ";

    public static final String ERROR_MESSAGE = "Аутентификация была не через JWT.";

    private Swagger() { }

}