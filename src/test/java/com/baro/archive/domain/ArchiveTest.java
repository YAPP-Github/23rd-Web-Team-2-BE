package com.baro.archive.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.domain.MemoContent;
import com.baro.memofolder.domain.MemoFolder;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArchiveTest {

    @Test
    void 메모_폴더를_변경한다() {
        // given
        var member = MemberFixture.memberWithNickname("baro");
        var oldMemoFolder = MemoFolder.of(member, "old");
        var newMemoFolder = MemoFolder.of(member, "new");
        var archive = new Archive(member, oldMemoFolder, MemoContent.from("content"));

        // when
        archive.changeMemoFolder(newMemoFolder);

        // then
        assertThat(archive.getMemoFolder()).isEqualTo(newMemoFolder);
    }

    @Test
    void 소유자인지_체크한다() {
        // given
        var member = MemberFixture.memberWithNickname("baro");
        var memoFolder = MemoFolder.of(member, "folder");
        var archive = new Archive(member, memoFolder, MemoContent.from("content"));

        // when & then
        assertThatCode(() -> archive.matchOwner(member.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void 소유자가_아니라면_예외를_반환한다() {
        // given
        var member = MemberFixture.memberWithNickname("baro");
        var memoFolder = MemoFolder.of(member, "folder");
        var archive = new Archive(member, memoFolder, MemoContent.from("content"));
        var otherMemberId = 999L;

        // when & then
        assertThatThrownBy(() -> archive.matchOwner(otherMemberId))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 아카이브_컨텐츠를_수정한다() {
        // given
        var member = MemberFixture.memberWithNickname("baro");
        var memoFolder = MemoFolder.of(member, "folder");
        var archive = new Archive(member, memoFolder, MemoContent.from("content"));
        MemoContent newContent = MemoContent.from("new content");

        // when
        archive.modifyContent(newContent);

        // then
        assertThat(archive.getContent()).isEqualTo(newContent);
    }
}
