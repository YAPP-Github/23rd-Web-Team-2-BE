package com.baro.member.domain;

import com.baro.common.utils.EmojiUtils;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MemberNickname {

    private static final int MAX_NICKNAME_SIZE = 30;

    private String nickname;

    private MemberNickname(String nickname) {
        validate(nickname);
        this.nickname = nickname;
    }

    private void validate(String content) {
        if (Objects.isNull(content)) {
            return;
        }

        if (content.isEmpty()) {
            throw new MemberException(MemberExceptionType.EMPTY_NICKNAME);
        }

        if (EmojiUtils.calculateLengthWithEmojis(content) > MAX_NICKNAME_SIZE) {
            throw new MemberException(MemberExceptionType.OVER_MAX_SIZE_NICKNAME);
        }
    }

    public static MemberNickname from(String nickname) {
        return new MemberNickname(nickname);
    }

    public String value() {
        return nickname;
    }
}
