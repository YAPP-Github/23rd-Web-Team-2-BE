package com.baro.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberNameAdjectives {

    EXAMPLE_ADJECTIVE1("형용사1"),
    EXAMPLE_ADJECTIVE2("형용사2"),
    EXAMPLE_ADJECTIVE3("형용사3"),
    EXAMPLE_ADJECTIVE4("형용사4"),
    EXAMPLE_ADJECTIVE5("형용사5"),
    EXAMPLE_ADJECTIVE6("형용사6"),
    EXAMPLE_ADJECTIVE7("형용사7"),
    EXAMPLE_ADJECTIVE8("형용사8"),
    EXAMPLE_ADJECTIVE9("형용사9"),
    ;

    private final String adjective;

    public static String pickRandom() {
        int randomIndex = (int) (Math.random() * MemberNameAdjectives.values().length);
        return MemberNameAdjectives.values()[randomIndex].getAdjective();
    }
}
