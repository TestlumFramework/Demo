package com.knubisoft.testapi.security.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.security.oauth.OAuthSecurityErrorHandler;

public class GoogleSecurityErrorHandler extends OAuthSecurityErrorHandler {
    public GoogleSecurityErrorHandler(final ObjectMapper objectMapper) {
        super(objectMapper, "Google");
    }
}
