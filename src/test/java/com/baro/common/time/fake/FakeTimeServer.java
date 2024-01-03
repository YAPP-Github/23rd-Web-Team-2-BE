package com.baro.common.time.fake;

import com.baro.common.time.TimeServer;

import java.time.Instant;
import java.time.LocalDateTime;

public class FakeTimeServer implements TimeServer {
    @Override
    public Instant now() {
        return Instant.from(LocalDateTime.of(2024, 1, 1, 0, 0, 0));
    }
}
