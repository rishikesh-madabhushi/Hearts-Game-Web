package com.llwantedll.webhearts.models.configs;

public class ConfigurationData {
    public static final String CROSS_ORIGIN_URL = "http://localhost:4200";
    public static final String WEBHEARTS_DATABASE = "webhearts";
    public static final String DATABASE_URL = "mongodb://localhost:27017";

    public static final String[] CORS_ALLOWED_METHODS =
            {"GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"};
    public static final String[] CORS_ALLOWED_HEADERS =
            {"X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"};
    public static final String CORS_ALLOWED_ORIGIN = "*";
    public static final String CORS_CONFIGURATION_PATH = "/**";

    public static final String STOMP_ALLOWED_ORIGINS = "*";
}
