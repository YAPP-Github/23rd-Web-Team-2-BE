package com.baro.memo.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.memo.exception.MemoException;
import com.baro.memo.exception.MemoExceptionType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemoContentTest {

    @Test
    void 메모_내용_생성() {
        // given
        String content = "끄적이는 메모 컨텐츠";

        // when & then
        assertThatCode(() -> MemoContent.from(content))
                .doesNotThrowAnyException();
    }

    @Test
    void 메모_내용_생성_최대_길이_초과() {
        // given
        String content = "끄적이는 메모 컨텐츠".repeat(500);

        // when & then
        assertThatThrownBy(() -> MemoContent.from(content))
                .isInstanceOf(MemoException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoExceptionType.OVER_MAX_SIZE_CONTENT);
    }
}
