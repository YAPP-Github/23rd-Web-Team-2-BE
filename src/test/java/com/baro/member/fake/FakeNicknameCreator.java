package com.baro.member.fake;

import com.baro.member.application.NicknameCreator;

public class FakeNicknameCreator implements NicknameCreator {
    @Override
    public String create() {
        return "nickname";
    }
}
