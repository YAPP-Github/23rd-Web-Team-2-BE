package com.baro.auth.application.oauth;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.application.oauth.dto.OAuthTokenInfo;
import com.baro.auth.domain.oauth.OAuthServiceType;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class OAuthInfoProvider {

    private final Map<OAuthServiceType, OAuthClient> clients;

    public OAuthInfoProvider(Set<OAuthClient> clients) {
        this.clients = clients.stream()
                .collect(toMap(OAuthClient::getOAuthService, identity()));
    }

    public String getSignInUrl(String oAuthService, String host) {
        OAuthClient client = getClient(oAuthService);
        return client.getSignInUrl(host);
    }

    public OAuthMemberInfo getMemberInfo(String oAuthService, String code) {
        OAuthClient client = getClient(oAuthService);
        OAuthTokenInfo oAuthTokenInfo = client.requestAccessToken(code);
        return client.requestMemberInfo(oAuthTokenInfo);
    }

    private OAuthClient getClient(String oauthType) {
        OAuthServiceType oAuthServiceType = OAuthServiceType.from(oauthType);
        return clients.get(oAuthServiceType);
    }
}
