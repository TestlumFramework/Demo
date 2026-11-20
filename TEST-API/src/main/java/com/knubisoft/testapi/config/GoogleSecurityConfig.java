package com.knubisoft.testapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.security.google.GoogleAuthenticationFilter;
import com.knubisoft.testapi.security.google.GoogleSecurityErrorHandler;
import com.knubisoft.testapi.service.GoogleProfileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "google.oauth.enabled", havingValue = "true")
public class GoogleSecurityConfig {

    @Bean
    public GoogleSecurityErrorHandler googleSecurityErrorHandler(final ObjectMapper objectMapper) {
        return new GoogleSecurityErrorHandler(objectMapper);
    }

    @Bean
    public GoogleAuthenticationFilter googleAuthenticationFilter(final GoogleProfileService profileService,
                                                                 final GoogleSecurityErrorHandler errorHandler) {
        return new GoogleAuthenticationFilter(profileService, errorHandler);
    }

    @Bean
    public FilterRegistrationBean<GoogleAuthenticationFilter> googleFilterRegistration(
            final GoogleAuthenticationFilter filter) {
        FilterRegistrationBean<GoogleAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain googleAuthFilterChain(final HttpSecurity http,
                                                     final GoogleAuthenticationFilter filter,
                                                     final GoogleSecurityErrorHandler errorHandler) throws Exception {
        return OAuthSecuritySupport.profileChain(http, "/api/test/auth/google/profile",
                filter, errorHandler);
    }
}
