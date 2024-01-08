package com.baro.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class RequestResolver {

    public String extractIp() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null) throw new IllegalArgumentException("비정상적인 요청입니다."); // TODO

        HttpServletRequest request = requestAttributes.getRequest();
        for (IpHeaderCandidates ipHeaderCandidate : IpHeaderCandidates.values()) {
            String ip = request.getHeader(ipHeaderCandidate.name());
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) return ip.split(",")[0];
        }
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
