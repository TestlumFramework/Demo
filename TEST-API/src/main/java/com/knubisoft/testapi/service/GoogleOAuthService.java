package com.knubisoft.testapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.config.GoogleOAuthConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@ConditionalOnProperty(name = "google.oauth.enabled", havingValue = "true")
public class GoogleOAuthService extends AbstractOAuthService<GoogleOAuthConfig, String> {

    public GoogleOAuthService(final GoogleOAuthConfig config,
            @Qualifier("googleOAuthRestTemplate") final RestTemplate restTemplate,
            final ObjectMapper objectMapper) {
        super(config, restTemplate, objectMapper);
    }

    @Override
    protected String providerName() {
        return "Google";
    }

    @Override
    protected String authorizationEndpoint() {
        return "https://accounts.google.com/o/oauth2/v2/auth";
    }

    @Override
    protected String tokenEndpoint() {
        return "https://oauth2.googleapis.com/token";
    }

    @Override
    protected String authorizationScope() {
        return "openid email profile";
    }

    @Override
    protected void customizeAuthorization(final UriComponentsBuilder builder) {
        builder.queryParam("access_type", "offline");
    }

    @Override
    protected String readTokens(final String body) {
        if (!StringUtils.hasText(body)) {
            throw invalidTokenResponse();
        }
        try {
            JsonNode response = objectMapper.readTree(body);
            JsonNode token = response == null ? null : response.get("refresh_token");
            if (response == null || !response.isObject() || response.has("error")
                    || token == null || !token.isTextual() || !StringUtils.hasText(token.textValue())) {
                throw invalidTokenResponse();
            }
            return token.textValue();
        } catch (JsonProcessingException ex) {
            throw invalidTokenResponse();
        }
    }

    private ResponseStatusException invalidTokenResponse() {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Google did not return a valid refresh token");
    }

}
