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
@ConditionalOnProperty(name = "google.oauth.enabled", havingValue = "true")
public class GoogleOAuthConfig implements OAuthClientConfig {

    @Value("${google.oauth.client-id:}")
    private String clientId;

    @Value("${google.oauth.client-secret:}")
    private String clientSecret;

    @Value("${google.oauth.redirect-uri:}")
    private String redirectUri;

    @PostConstruct
    public void validate() {
        requireValue(clientId, "google.oauth.client-id");
        requireValue(clientSecret, "google.oauth.client-secret");
        requireValue(redirectUri, "google.oauth.redirect-uri");
    }

    @Bean(name = "googleOAuthRestTemplate")
    public RestTemplate googleOAuthRestTemplate() {
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
