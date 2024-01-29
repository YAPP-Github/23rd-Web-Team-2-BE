package com.baro.archive.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArchiveTabTest {

    @Test
    void 해당하는_아카이브_탭을_반환한다() {
        // given
        var tab = "all";

        // when
        var result = ArchiveTab.from(tab);

        // then
        assertThat(result).isEqualTo(ArchiveTab.ALL);
    }

    @Test
    void 해당하는_아카이브_탭이_없는_경우_에러를_반환한다() {
        // given
        var tab = "meme";

        // when & then
        assertThatThrownBy(() -> ArchiveTab.from(tab))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.NOT_EXIST_TAB);
    }
}
