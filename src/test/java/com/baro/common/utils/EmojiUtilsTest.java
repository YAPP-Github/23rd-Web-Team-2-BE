package com.baro.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EmojiUtilsTest {

    @ParameterizedTest
    @CsvSource(value = {
            "안녕하세요😀, 6",
            "😀안녕 하세요😀, 8",
            "👩‍👩‍👧‍👦안녕하세요🌍, 7",
            "안녕하세요, 5",
            "안녕하세요😀😀😀😀😀, 10"
    })
    void 이모지_길이_계산(String textWithEmoji, int expectedLength) {
        // when
        int actualLength = EmojiUtils.calculateLengthWithEmojis(textWithEmoji);

        // then
        assertThat(actualLength).isEqualTo(expectedLength);
    }
}
