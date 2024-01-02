package com.baro.auth.fake.oauth;

import com.baro.member.application.NicknameCreator;

public class FakeNicknameCreator implements NicknameCreator {
    @Override
    public String createRandomNickname() {
        return "nickname";
    }
}
