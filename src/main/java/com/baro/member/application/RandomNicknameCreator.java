package com.baro.member.application;

import com.baro.member.domain.MemberNameAdjectives;
import com.baro.member.domain.MemberNameNouns;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class RandomNicknameCreator implements NicknameCreator {

    @Override
    public String createRandomNickname() {
        return MemberNameAdjectives.values()[(int) (Math.random() * MemberNameAdjectives.values().length)].getAdjective() +
                MemberNameNouns.values()[(int) (Math.random() * MemberNameNouns.values().length)].getNoun();
    }
}
