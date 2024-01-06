package com.baro.member.application;

import com.baro.member.domain.MemberNameAdjectives;
import com.baro.member.domain.MemberNameNouns;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class RandomNicknameCreator implements NicknameCreator {

    @Override
    public String create() {
        return MemberNameAdjectives.pickRandom() + MemberNameNouns.pickRandom();
    }
}
