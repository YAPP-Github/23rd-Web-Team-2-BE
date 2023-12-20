package com.baro.oauth.application;

import com.baro.oauth.domain.OAuthServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OAuthService {

    private final OAuthClientComponents clientComponents;

    @Transactional(readOnly = true)
    public String getSignInUrl(final String oAuthService) {
        OAuthServiceType oAuthServiceType = OAuthServiceType.from(oAuthService);
        OAuthClient client = clientComponents.getClient(oAuthServiceType);
        return client.getSignInUrl();
    }
}
