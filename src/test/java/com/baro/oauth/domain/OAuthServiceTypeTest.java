package com.baro.oauth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OAuthServiceTypeTest {

    @Test
    void Service_이름으로_타입_조회_테스트() {
        // given
        String requestServiceName = "kakao";

        // when
        OAuthServiceType oAuthServiceType = OAuthServiceType.from(requestServiceName);

        // then
        assertThat(oAuthServiceType).isEqualTo(OAuthServiceType.KAKAO);
    }
}
