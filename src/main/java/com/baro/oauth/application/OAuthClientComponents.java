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

    /**
     * p3.Enum 과 OAuthClient가 1:1 매칭이 보장되는 방식이 아니다보니,
     * 유지보수 단계 실수가 발생할 수 있는 구조로 보입니다.
     * - 같은 OAuthServiceType을 가지고 있는 클래스가 있을 수 있고, (Enum 중복)
     * - Enum 값만 추가하고 OAuthClient는 없는 경우도 발생 (너무 특이 케이스이긴 합니다만)
     * 로그인 서비스이다 보니 발생하지 않을 수도 있지만
     * Enum -> Serivce 를 가져오는 패턴은 간간히 있다보니 리뷰로 남깁니다.
     */
    public OAuthClientComponents(Set<OAuthClient> clients) {
        this.clients = clients.stream()
                .collect(toMap(OAuthClient::getOAuthService, identity()));
    }

    public OAuthClient getClient(OAuthServiceType oAuthServiceType) {
        return clients.get(oAuthServiceType);
    }
}
