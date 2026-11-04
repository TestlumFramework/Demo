package com.knubisoft.testapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.config.MicrosoftOAuthConfig;
import com.knubisoft.testapi.dto.auth.microsoft.MicrosoftCredentialsResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@ConditionalOnProperty(name = "microsoft.oauth.enabled", havingValue = "true")
public class MicrosoftOAuthService extends AbstractOAuthService<MicrosoftOAuthConfig, MicrosoftCredentialsResponse> {

    public MicrosoftOAuthService(final MicrosoftOAuthConfig config,
            @Qualifier("microsoftOAuthRestTemplate") final RestTemplate restTemplate,
            final ObjectMapper objectMapper) {
        super(config, restTemplate, objectMapper);
    }

    @Override
    protected String providerName() {
        return "Microsoft";
    }

    @Override
    protected String authorizationEndpoint() {
        return endpoint("authorize");
    }

    @Override
    protected String tokenEndpoint() {
        return endpoint("token");
    }

    private String endpoint(final String action) {
        return UriComponentsBuilder.fromHttpUrl("https://login.microsoftonline.com")
                .pathSegment(config.getTenant(), "oauth2", "v2.0", action)
                .build().encode().toUriString();
    }

    @Override
    protected String authorizationScope() {
        return "openid profile offline_access " + config.getScope();
    }

    @Override
    protected void customizeAuthorization(final UriComponentsBuilder builder) {
        builder.queryParam("response_mode", "query");
    }

    @Override
    protected void customizeTokenForm(final MultiValueMap<String, String> form) {
        form.add("scope", authorizationScope());
    }

    @Override
    protected MicrosoftCredentialsResponse readTokens(final String body) {
        if (!StringUtils.hasText(body)) {
            throw invalidTokenResponse();
        }
        try {
            JsonNode response = objectMapper.readTree(body);
            if (response == null || !response.isObject() || response.has("error")) {
                throw invalidTokenResponse();
            }
            String refreshToken = tokenField(response, "refresh_token");
            String accessToken = tokenField(response, "access_token");
            String tokenType = tokenField(response, "token_type");
            JsonNode expiresIn = response.get("expires_in");
            if (!"Bearer".equalsIgnoreCase(tokenType) || expiresIn == null
                    || !expiresIn.isIntegralNumber() || !expiresIn.canConvertToLong() || expiresIn.longValue() <= 0) {
                throw invalidTokenResponse();
            }
            return new MicrosoftCredentialsResponse(refreshToken, accessToken, "Bearer", expiresIn.longValue());
        } catch (JsonProcessingException ex) {
            throw invalidTokenResponse();
        }
    }

    private String tokenField(final JsonNode response, final String name) {
        JsonNode value = response.get(name);
        if (value == null || !value.isTextual() || !StringUtils.hasText(value.textValue())) {
            throw invalidTokenResponse();
        }
        return value.textValue();
    }

    private ResponseStatusException invalidTokenResponse() {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Microsoft did not return valid access and refresh tokens");
    }

}
