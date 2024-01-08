package com.baro.auth.presentation;

import com.baro.auth.exception.AuthException;
import com.baro.auth.exception.AuthExceptionType;
import jakarta.servlet.http.HttpServletRequest;
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
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            String requestIp = ip.split(",")[0];
            log.info("RequestResolver::extractIp -> {}", requestIp);
            return requestIp;
        }
        log.info("RequestResolver::extractIp -> {}", request.getRemoteAddr());
        return request.getRemoteAddr();
    }
}
