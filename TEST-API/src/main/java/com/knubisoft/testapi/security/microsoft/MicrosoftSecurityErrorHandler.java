package com.knubisoft.testapi.security.microsoft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.security.oauth.OAuthSecurityErrorHandler;

public class MicrosoftSecurityErrorHandler extends OAuthSecurityErrorHandler {
    public MicrosoftSecurityErrorHandler(final ObjectMapper objectMapper) {
        super(objectMapper, "Microsoft");
    }
}
