package com.knubisoft.testapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.testapi.security.microsoft.MicrosoftAuthenticationFilter;
import com.knubisoft.testapi.security.microsoft.MicrosoftSecurityErrorHandler;
import com.knubisoft.testapi.service.MicrosoftProfileService;
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
@ConditionalOnProperty(name = "microsoft.oauth.enabled", havingValue = "true")
public class MicrosoftSecurityConfig {

    @Bean
    public MicrosoftSecurityErrorHandler microsoftSecurityErrorHandler(final ObjectMapper objectMapper) {
        return new MicrosoftSecurityErrorHandler(objectMapper);
    }

    @Bean
    public MicrosoftAuthenticationFilter microsoftAuthenticationFilter(final MicrosoftProfileService profileService,
                                                               final MicrosoftSecurityErrorHandler errorHandler) {
        return new MicrosoftAuthenticationFilter(profileService, errorHandler);
    }

    @Bean
    public FilterRegistrationBean<MicrosoftAuthenticationFilter> microsoftFilterRegistration(
            final MicrosoftAuthenticationFilter filter) {
        FilterRegistrationBean<MicrosoftAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain microsoftAuthFilterChain(final HttpSecurity http,
                                                    final MicrosoftAuthenticationFilter filter,
                                                    final MicrosoftSecurityErrorHandler errorHandler) throws Exception {
        return OAuthSecuritySupport.profileChain(http, "/api/test/auth/microsoft/profile",
                filter, errorHandler);
    }
}
