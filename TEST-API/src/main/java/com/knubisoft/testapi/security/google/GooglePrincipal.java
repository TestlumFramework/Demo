package com.knubisoft.testapi.security.google;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.security.Principal;

@Getter
@RequiredArgsConstructor
public class GooglePrincipal implements Principal, Serializable {

    private static final long serialVersionUID = 1L;

    private final String sub;
    private final String name;
    private final String email;
}
