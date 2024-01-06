package com.baro.common.time.fake;

import com.baro.common.time.TimeServer;

import java.time.Instant;

public class FakeTimeServer implements TimeServer {

    private Instant now;

    public FakeTimeServer(Instant now) {
        this.now = now;
    }

    @Override
    public Instant now() {
        return now;
    }
}
