package com.knubisoft.testapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.security.google.GooglePrincipal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@ConditionalOnProperty(name = "google.oauth.enabled", havingValue = "true")
public class GoogleProfileService extends AbstractOAuthProfileService<GooglePrincipal> {

    private static final String USERINFO_ENDPOINT = "https://openidconnect.googleapis.com/v1/userinfo";

    public GoogleProfileService(@Qualifier("googleOAuthRestTemplate") final RestTemplate restTemplate,
                                  final ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    @Override
    protected String providerName() {
        return "Google";
    }

    @Override
    protected String profileEndpoint() {
        return USERINFO_ENDPOINT;
    }

    @Override
    protected GooglePrincipal readProfile(final String body) {
        if (!StringUtils.hasText(body)) {
            throw invalidProfileResponse();
        }
        try {
            JsonNode response = objectMapper.readTree(body);
            if (response == null || !response.isObject() || response.has("error")) {
                throw invalidProfileResponse();
            }
            JsonNode sub = response.get("sub");
            JsonNode name = response.get("name");
            JsonNode email = response.get("email");
            if (sub == null || !sub.isTextual() || !StringUtils.hasText(sub.textValue())
                    || name == null || !name.isTextual() || !StringUtils.hasText(name.textValue())
                    || email == null || !email.isTextual() || !StringUtils.hasText(email.textValue())) {
                throw invalidProfileResponse();
            }
            return new GooglePrincipal(sub.textValue(), name.textValue(), email.textValue());
        } catch (JsonProcessingException ex) {
            throw invalidProfileResponse();
        }
    }

}
