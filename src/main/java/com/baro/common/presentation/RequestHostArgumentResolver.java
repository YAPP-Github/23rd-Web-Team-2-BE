package com.baro.common.presentation;

import static com.baro.common.exception.CommonRequestExceptionType.REQUIRED_HEADER_EXCEPTION;

import com.baro.common.exception.RequestException;
import com.baro.common.exception.RequestExceptionType;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class RequestHostArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String HOST_HEADER_KEY = "Origin";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestHost.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String host = webRequest.getHeader(HOST_HEADER_KEY);
        validateHeaderIsNonNull(host);
        return host;
    }

    private void validateHeaderIsNonNull(String host) {
        if (Objects.nonNull(host)) {
            return;
        }

        throw new RequestException() {
            @Override
            public RequestExceptionType exceptionType() {
                return REQUIRED_HEADER_EXCEPTION;
            }
        };
    }
}
