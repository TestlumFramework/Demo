package com.knubisoft.testapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

public abstract class AbstractOAuthProfileService<P extends Principal> {

    private final RestTemplate restTemplate;
    protected final ObjectMapper objectMapper;

    protected AbstractOAuthProfileService(final RestTemplate restTemplate, final ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    protected abstract String providerName();
    protected abstract String profileEndpoint();
    protected abstract P readProfile(String body);

    public final P profile(final String authorizationHeader) {
        String token = readBearerToken(authorizationHeader);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String body;
        try {
            body = restTemplate.exchange(profileEndpoint(), HttpMethod.GET,
                    new HttpEntity<Void>(headers), String.class).getBody();
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired " + providerName() + " access token");
            }
            if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, providerName() + " profile access was denied");
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, providerName() + " profile request failed");
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, providerName() + " profile request failed");
        }
        return readProfile(body);
    }

    private String readBearerToken(final String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing " + providerName() + " Authorization header");
        }
        if (!authorizationHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expected a " + providerName() + " Bearer token");
        }
        String token = authorizationHeader.substring(7);
        if (!StringUtils.hasText(token) || !token.matches("\\S+")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expected a " + providerName() + " Bearer token");
        }
        return token;
    }

    protected final ResponseStatusException invalidProfileResponse() {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, providerName() + " did not return a valid user profile");
    }

}
