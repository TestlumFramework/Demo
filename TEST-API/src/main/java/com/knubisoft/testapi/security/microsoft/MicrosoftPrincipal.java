package com.knubisoft.testapi.security.microsoft;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.security.Principal;

@Getter
@RequiredArgsConstructor
public class MicrosoftPrincipal implements Principal, Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;
    private final String email;
}
