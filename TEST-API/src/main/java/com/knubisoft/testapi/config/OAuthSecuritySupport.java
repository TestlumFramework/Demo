package com.knubisoft.testapi.config;

import com.knubisoft.testapi.security.oauth.OAuthAuthenticationFilter;
import com.knubisoft.testapi.security.oauth.OAuthSecurityErrorHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

final class OAuthSecuritySupport {
    private OAuthSecuritySupport() {}

    static SecurityFilterChain profileChain(final HttpSecurity http, final String path,
                                            final OAuthAuthenticationFilter filter,
                                            final OAuthSecurityErrorHandler errorHandler) throws Exception {
        http.requestMatchers()
                .antMatchers(path, path + "/")
                .and()
                .csrf().disable()
                .requestCache().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(errorHandler)
                .accessDeniedHandler(errorHandler)
                .and()
                .authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
