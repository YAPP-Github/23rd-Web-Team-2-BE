package com.baro.oauth.application;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import com.baro.oauth.domain.OAuthServiceType;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class OAuthClientComponents {

    private final Map<OAuthServiceType, OAuthClient> clients;

    public OAuthClientComponents(Set<OAuthClient> clients) {
        this.clients = clients.stream()
                .collect(toMap(OAuthClient::getOAuthService, identity()));
    }

    public OAuthClient getClient(OAuthServiceType oAuthServiceType) {
        return clients.get(oAuthServiceType);
    }
}
