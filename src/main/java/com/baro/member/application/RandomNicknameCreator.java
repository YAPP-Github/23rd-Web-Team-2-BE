package com.baro.member.application;

import com.baro.member.domain.Adjectives;
import com.baro.member.domain.Nouns;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class RandomNicknameCreator implements NicknameCreator {

    @Override
    public String createRandomNickname() {
        return Adjectives.values()[(int) (Math.random() * Adjectives.values().length)].getAdjective() +
                Nouns.values()[(int) (Math.random() * Nouns.values().length)].getNoun();
    }
}
