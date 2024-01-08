package com.baro.auth.presentation;

import com.baro.auth.exception.AuthException;
import com.baro.auth.exception.AuthExceptionType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
public class RequestResolver {

    public String extractIp() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null) throw new AuthException(AuthExceptionType.IP_ADDRESS_DOES_NOT_EXIST);

        HttpServletRequest request = requestAttributes.getRequest();
        for (IpHeaderCandidates ipHeaderCandidate : IpHeaderCandidates.values()) {
            String ip = request.getHeader(ipHeaderCandidate.getName());
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                String s = ip.split(",")[0];
                log.info("RequestResolver::extractIp -> {}", s);
                return s;
            }
        }
        log.info("RequestResolver::extractIp -> {}", request.getRemoteAddr());
        return request.getRemoteAddr();
    }

    @RequiredArgsConstructor
    @Getter
    enum IpHeaderCandidates {
        X_FORWARDED_FOR("X-Forwarded-For"),
        PROXY_CLIENT_IP("Proxy-Client-IP"),
        WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
        HTTP_CLIENT_IP("HTTP_CLIENT_IP"),
        HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR"),
        HTTP_X_FORWARDED("HTTP_X_FORWARDED"),
        HTTP_X_CLUSTER_CLIENT_IP("HTTP_X_CLUSTER_CLIENT_IP"),
        HTTP_FORWARDED_FOR("HTTP_FORWARDED_FOR"),
        HTTP_FORWARDED("HTTP_FORWARDED"),
        HTTP_VIA("HTTP_VIA"),
        REMOTE_ADDR("REMOTE_ADDR"),
        ;

        private final String name;
    }
}
