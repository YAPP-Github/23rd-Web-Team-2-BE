package com.baro.common.config;

import com.baro.auth.presentation.AuthInterceptor;
import com.baro.auth.presentation.AuthenticationArgumentResolver;
import com.baro.common.presentation.RequestHostArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationArgumentResolver authenticationArgumentResolver;
    private final AuthInterceptor authInterceptor;
    private final RequestHostArgumentResolver requestHostArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
        resolvers.add(requestHostArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/docs/**")
                .excludePathPatterns("/auth/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/test/**");
    }
}
