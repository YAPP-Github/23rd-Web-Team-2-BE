package com.baro.oauth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.oauth.exception.OAuthException;
import com.baro.oauth.exception.OAuthExceptionType;
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

    @Test
    void Google_Service_이름으로_타입_조회_테스트() {
        // given
        String requestServiceName = "google";

        // when
        OAuthServiceType oAuthServiceType = OAuthServiceType.from(requestServiceName);

        // then
        assertThat(oAuthServiceType).isEqualTo(OAuthServiceType.GOOGLE);
    }

    @Test
    void 지원_하지_않는_서비스_조회시_예외_발생() {
        // given
        String notSupportedServiceName = "notSupportedService";

        // when & then
        assertThatThrownBy(() -> OAuthServiceType.from(notSupportedServiceName))
                .isInstanceOf(OAuthException.class)
                .extracting("exceptionType")
                .isEqualTo(OAuthExceptionType.NOT_SUPPORTED_OAUTH_TYPE);
    }
}
