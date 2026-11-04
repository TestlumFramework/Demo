package com.knubisoft.testapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Getter
@Configuration
@ConditionalOnProperty(name = "microsoft.oauth.enabled", havingValue = "true")
public class MicrosoftOAuthConfig implements OAuthClientConfig {

    @Value("${microsoft.oauth.client-id:}")
    private String clientId;

    @Value("${microsoft.oauth.client-secret:}")
    private String clientSecret;

    @Value("${microsoft.oauth.tenant:common}")
    private String tenant;

    @Value("${microsoft.oauth.redirect-uri:}")
    private String redirectUri;

    @Value("${microsoft.oauth.scope:https://graph.microsoft.com/User.Read}")
    private String scope;

    @PostConstruct
    public void validate() {
        requireValue(clientId, "microsoft.oauth.client-id");
        requireValue(clientSecret, "microsoft.oauth.client-secret");
        requireValue(tenant, "microsoft.oauth.tenant");
        requireValue(redirectUri, "microsoft.oauth.redirect-uri");
        requireValue(scope, "microsoft.oauth.scope");
        if (!tenant.matches("[A-Za-z0-9][A-Za-z0-9.-]*")) {
            throw new IllegalStateException("Invalid configuration: microsoft.oauth.tenant");
        }
    }

    @Bean(name = "microsoftOAuthRestTemplate")
    public RestTemplate microsoftOAuthRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(30_000);
        return new RestTemplate(factory);
    }

    private void requireValue(final String value, final String propertyName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException("Missing required configuration: " + propertyName);
        }
    }
}
