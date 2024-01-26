package com.baro.memofolder.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.member.domain.Member;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemoFolderTest {

    @Test
    void 메모_폴더의_소유자가_일치한다() {
        // given
        Member member = new Member(1L, "name", "email", "nickname", "oAuthId", "oAuthServiceType");
        MemoFolder memoFolder = MemoFolder.of(member, "폴더이름");

        // when & then
        assertThatCode(() -> memoFolder.matchOwner(1L))
                .doesNotThrowAnyException();
    }

    @Test
    void 메모_폴더의_소유자가_일치하지_않는다() {
        // given
        Member member = new Member(1L, "name", "email", "nickname", "oAuthId", "oAuthServiceType");
        MemoFolder memoFolder = MemoFolder.of(member, "폴더이름");

        // when & then
        assertThatThrownBy(() -> memoFolder.matchOwner(999L))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 메모_폴더의_이름을_변경한다() {
        // given
        Member member = new Member(1L, "name", "email", "nickname", "oAuthId", "oAuthServiceType");
        MemoFolder memoFolder = MemoFolder.of(member, "폴더이름");

        // when & then
        assertThatCode(() -> memoFolder.rename("변경된폴더이름"))
                .doesNotThrowAnyException();
    }

    @Test
    void 메모_폴더_이름_변경시_최대_길이_검증() {
        // given
        Member member = new Member(1L, "name", "email", "nickname", "oAuthId", "oAuthServiceType");
        MemoFolder memoFolder = MemoFolder.of(member, "폴더이름");

        // when & then
        assertThatThrownBy(() -> memoFolder.rename("1".repeat(11)))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.OVER_MAX_SIZE_NAME);
    }

    @Test
    void 메모_폴더_이름_변경시_빈_문자열_검증() {
        // given
        Member member = new Member(1L, "name", "email", "nickname", "oAuthId", "oAuthServiceType");
        MemoFolder memoFolder = MemoFolder.of(member, "폴더이름");

        // when & then
        assertThatThrownBy(() -> memoFolder.rename(""))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.EMPTY_NAME);
    }
}
