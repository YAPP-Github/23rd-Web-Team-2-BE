package com.baro.memofolder.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemoFolderNameTest {

    @Test
    void 메모_폴더_이름_생성() {
        // given
        String memoFolderName = "회사생활👔";

        // when
        assertThatCode(() -> MemoFolderName.from(memoFolderName))
                .doesNotThrowAnyException();
    }

    @Test
    void 메모_폴더_이름_길이_검증() {
        // given
        String overSizeName = "1".repeat(11);

        // when & then
        assertThatThrownBy(() -> MemoFolderName.from(overSizeName))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.OVER_MAX_SIZE_NAME);
    }

    @Test
    void 이모지가_포함된_메모_폴더_이름_길이_검증() {
        // given
        String overSizeName = "회사생활은재미없겠지만해야겠지👔👔👔";

        // when & then
        assertThatThrownBy(() -> MemoFolderName.from(overSizeName))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.OVER_MAX_SIZE_NAME);
    }

    @Test
    void 메모_폴더_이름_길이_검증_빈_문자열() {
        // given
        String emptyName = "";

        // when & then
        assertThatThrownBy(() -> MemoFolderName.from(emptyName))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.EMPTY_NAME);
    }
}
