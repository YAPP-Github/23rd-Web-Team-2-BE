package com.baro.memo.domain;

import com.baro.memo.exception.MemoException;
import com.baro.memo.exception.MemoExceptionType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MemoContent {

    private static final int MAX_CONTENT_SIZE = 500;

    @Column(length = MAX_CONTENT_SIZE)
    private String content;

    private MemoContent(String content) {
        validate(content);
        this.content = content;
    }

    private void validate(String content) {
        if (content.length() > MAX_CONTENT_SIZE) {
            throw new MemoException(MemoExceptionType.OVER_MAX_SIZE_CONTENT);
        }
    }

    public static MemoContent from(String content) {
        return new MemoContent(content);
    }
}
