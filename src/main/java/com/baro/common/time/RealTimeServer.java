package com.baro.common.time;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RealTimeServer implements TimeServer {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
