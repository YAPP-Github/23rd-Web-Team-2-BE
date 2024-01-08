package com.baro.auth.infra.oauth.config;

import com.baro.auth.infra.oauth.naver.NaverRequestApi;
import com.baro.auth.infra.oauth.google.GoogleRequestApi;
import com.baro.auth.infra.oauth.kakao.KakaoRequestApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    public KakaoRequestApi kakaoApiClient() {
        return createHttpInterface(KakaoRequestApi.class);
    }

    @Bean
    public NaverRequestApi naverRequestApi() {
        return createHttpInterface(NaverRequestApi.class);
    }

    @Bean
    public GoogleRequestApi googleRequestApi() {
        return createHttpInterface(GoogleRequestApi.class);
    }

    private <T> T createHttpInterface(Class<T> clazz) {
        WebClient webClient = WebClient.create();
        HttpServiceProxyFactory build = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return build.createClient(clazz);
    }
}
