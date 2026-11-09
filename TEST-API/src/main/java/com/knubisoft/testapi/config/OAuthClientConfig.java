package com.knubisoft.testapi.config;

public interface OAuthClientConfig {
    String getClientId();
    String getClientSecret();
    String getRedirectUri();
}
