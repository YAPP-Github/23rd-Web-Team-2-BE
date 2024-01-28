package com.baro.archive.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
}
