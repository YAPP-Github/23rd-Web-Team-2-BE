package com.baro.memofolder.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MemoFolderNameTest {

    @Test
    void 메모_폴더_이름_길이_검증() {
        // given
        String overSizeName = "1".repeat(21);

        // when & then
        assertThatThrownBy(() -> MemoFolderName.from(overSizeName))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.OVER_MAX_SIZE_NAME);
    }
}
