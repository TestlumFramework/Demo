package com.knubisoft.testapi.dto.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtProtectedResourceResponse {
    private List<String> values;
}