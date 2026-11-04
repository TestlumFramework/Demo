package com.knubisoft.testapi.security.google;

import com.knubisoft.testapi.security.oauth.OAuthAuthenticationFilter;
import com.knubisoft.testapi.service.GoogleProfileService;

public class GoogleAuthenticationFilter extends OAuthAuthenticationFilter {
    public GoogleAuthenticationFilter(final GoogleProfileService profileService,
                                         final GoogleSecurityErrorHandler errorHandler) {
        super(profileService, errorHandler);
    }
}
