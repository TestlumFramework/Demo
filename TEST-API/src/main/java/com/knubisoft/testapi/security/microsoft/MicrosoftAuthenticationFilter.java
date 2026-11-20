package com.knubisoft.testapi.security.microsoft;

import com.knubisoft.testapi.security.oauth.OAuthAuthenticationFilter;
import com.knubisoft.testapi.service.MicrosoftProfileService;

public class MicrosoftAuthenticationFilter extends OAuthAuthenticationFilter {
    public MicrosoftAuthenticationFilter(final MicrosoftProfileService profileService,
                                         final MicrosoftSecurityErrorHandler errorHandler) {
        super(profileService, errorHandler);
    }
}
