package com.baro.memo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.archive.domain.Archive;
import com.baro.member.domain.Member;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.exception.TemporalMemoException;
import com.baro.memo.exception.TemporalMemoExceptionType;
import com.baro.memofolder.domain.MemoFolder;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TemporalMemoTest {

    @Test
    void 컨텐츠를_수정한다() {
        // given
        TemporalMemo temporalMemo = TemporalMemo.of(MemberFixture.memberWithNickname("바로"), "메모");
        String content = "수정된 메모";

        // when
        temporalMemo.updateContent(MemoContent.from(content));

        // then
        assertThat(temporalMemo.getContent().value()).isEqualTo(content);
    }

    @Test
    void 끄적이는_메모의_권한을_확인한다() {
        // given
        Member memberWithId = new Member(
                1L,
                "name",
                "email",
                "nickname",
                "oAuthId",
                "oAuthServiceType"
        );
        TemporalMemo temporalMemo = TemporalMemo.of(memberWithId, "메모");

        // when & then
        assertThatCode(() -> temporalMemo.matchOwner(memberWithId.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void 끄적이는_메모의_권한이_없는_경우_예외를_반환한다() {
        // given
        Member memberWithId = new Member(
                1L,
                "name",
                "email",
                "nickname",
                "oAuthId",
                "oAuthServiceType"
        );
        TemporalMemo temporalMemo = TemporalMemo.of(memberWithId, "메모");

        // when & then
        assertThatCode(() -> temporalMemo.matchOwner(2L))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 끄적이는_메모를_메모로_아카이빙한다() {
        // given
        Member memberWithId = new Member(
                1L,
                "name",
                "email",
                "nickname",
                "oAuthId",
                "oAuthServiceType"
        );
        TemporalMemo temporalMemo = TemporalMemo.of(MemberFixture.memberWithNickname("바로"), "메모");
        MemoFolder memoFolder = MemoFolder.defaultFolder(memberWithId);
        Archive archive = new Archive(memberWithId, memoFolder, MemoContent.from("메모"));

        // when
        temporalMemo.archived(archive);

        // then
        assertThat(temporalMemo.getArchive()).isEqualTo(archive);
    }

    @Test
    void 끄적이는_메모의_맞춤법_검사_반영_여부를_확인한다() {
        // given
        TemporalMemo temporalMemo = TemporalMemo.of(MemberFixture.memberWithNickname("바로"), "메모");

        // when
        boolean isCorrected = temporalMemo.isCorrected();

        // then
        assertThat(isCorrected).isFalse();
    }

    @Test
    void 끄적이는_메모의_맞춤법_검사_결과를_반영한다() {
        // given
        TemporalMemo temporalMemo = TemporalMemo.of(MemberFixture.memberWithNickname("바로"), "메모");
        MemoContent correctionContent = MemoContent.from("메모");

        // when
        temporalMemo.applyCorrection(correctionContent);

        // then
        assertAll(
                () -> assertThat(temporalMemo.getCorrectionContent()).isEqualTo(correctionContent),
                () -> assertThat(temporalMemo.isCorrected()).isTrue()
        );
    }

    @Test
    void 이미_맞춤법_검사_결과가_반영된_끄적이는_메모에_결과_반영시_예외를_반환한다() {
        // given
        TemporalMemo temporalMemo = TemporalMemo.of(MemberFixture.memberWithNickname("바로"), "메모");
        MemoContent correctionContent = MemoContent.from("메모");
        temporalMemo.applyCorrection(correctionContent);

        // when & then
        assertThatCode(() -> temporalMemo.applyCorrection(correctionContent))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.ALREADY_CORRECTED);
    }
}
