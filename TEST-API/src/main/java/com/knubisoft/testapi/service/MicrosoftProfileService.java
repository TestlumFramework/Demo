package com.knubisoft.testapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.security.microsoft.MicrosoftPrincipal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@ConditionalOnProperty(name = "microsoft.oauth.enabled", havingValue = "true")
public class MicrosoftProfileService extends AbstractOAuthProfileService<MicrosoftPrincipal> {

    private static final String USERINFO_ENDPOINT = "https://graph.microsoft.com/v1.0/me?$select=id,displayName,mail,userPrincipalName";

    public MicrosoftProfileService(@Qualifier("microsoftOAuthRestTemplate") final RestTemplate restTemplate,
                                  final ObjectMapper objectMapper) {
        super(restTemplate, objectMapper);
    }

    @Override
    protected String providerName() {
        return "Microsoft";
    }

    @Override
    protected String profileEndpoint() {
        return USERINFO_ENDPOINT;
    }

    @Override
    protected MicrosoftPrincipal readProfile(final String body) {
        if (!StringUtils.hasText(body)) {
            throw invalidProfileResponse();
        }
        try {
            JsonNode response = objectMapper.readTree(body);
            if (response == null || !response.isObject() || response.has("error")) {
                throw invalidProfileResponse();
            }
            JsonNode id = response.get("id");
            JsonNode name = response.get("displayName");
            JsonNode email = response.get("mail");
            if (email == null || !email.isTextual() || !StringUtils.hasText(email.textValue())) {
                email = response.get("userPrincipalName");
            }
            if (id == null || !id.isTextual() || !StringUtils.hasText(id.textValue())
                    || name == null || !name.isTextual() || !StringUtils.hasText(name.textValue())
                    || email == null || !email.isTextual() || !StringUtils.hasText(email.textValue())) {
                throw invalidProfileResponse();
            }
            return new MicrosoftPrincipal(id.textValue(), name.textValue(), email.textValue());
        } catch (JsonProcessingException ex) {
            throw invalidProfileResponse();
        }
    }

}
