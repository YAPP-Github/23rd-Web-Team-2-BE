package com.baro.archive.application;

import static com.baro.archive.fixture.ArchiveFixture.끄적이는_아카이브1;
import static com.baro.archive.fixture.ArchiveFixture.끄적이는_아카이브2;
import static com.baro.archive.fixture.ArchiveFixture.참고하는_아카이브1;
import static com.baro.archive.fixture.ArchiveFixture.참고하는_아카이브2;
import static com.baro.template.fixture.TemplateFixture.감사전하기;
import static com.baro.template.fixture.TemplateFixture.보고하기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.archive.application.dto.ArchiveUnitResult;
import com.baro.archive.application.dto.DeleteArchiveCommand;
import com.baro.archive.application.dto.GetArchiveQuery;
import com.baro.archive.application.dto.ModifyArchiveCommand;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.domain.ArchiveTab;
import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import com.baro.archive.fake.FakeArchiveRepository;
import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.domain.MemoContent;
import com.baro.memo.exception.MemoException;
import com.baro.memo.exception.MemoExceptionType;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.fake.FakeMemoFolderRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ArchiveServiceTest {

    private ArchiveService archiveService;
    private ArchiveRepository archiveRepository;
    private MemberRepository memberRepository;
    private MemoFolderRepository memoFolderRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        memoFolderRepository = new FakeMemoFolderRepository();
        archiveRepository = new FakeArchiveRepository();
        archiveService = new ArchiveService(memberRepository, memoFolderRepository, archiveRepository);
    }

    @Test
    void 전체_아카이브_조회() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive1 = archiveRepository.save(끄적이는_아카이브1(member, memoFolder));
        var archive2 = archiveRepository.save(끄적이는_아카이브2(member, memoFolder));
        var archive3 = archiveRepository.save(참고하는_아카이브1(member, memoFolder, 보고하기()));
        var archive4 = archiveRepository.save(참고하는_아카이브2(member, memoFolder, 감사전하기()));
        archive1.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 0, 0));
        archive2.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 5, 0));
        archive3.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 10, 0));
        archive4.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 15, 0));
        var query = new GetArchiveQuery(member.getId(), memoFolder.getId(), ArchiveTab.ALL);
        System.out.println(archive1.getCreatedAt());

        // when
        var archives = archiveService.getArchive(query);

        // then
        assertAll(
                () -> assertThat(archives).hasSize(4),
                () -> assertThat(archives).containsExactly(
                        ArchiveUnitResult.of(archive4),
                        ArchiveUnitResult.of(archive3),
                        ArchiveUnitResult.of(archive2),
                        ArchiveUnitResult.of(archive1)
                )
        );
    }

    @Test
    void 끄적이는_아카이브_조회() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive1 = archiveRepository.save(끄적이는_아카이브1(member, memoFolder));
        var archive2 = archiveRepository.save(끄적이는_아카이브2(member, memoFolder));
        var archive3 = archiveRepository.save(참고하는_아카이브1(member, memoFolder, 보고하기()));
        var archive4 = archiveRepository.save(참고하는_아카이브2(member, memoFolder, 감사전하기()));
        archive1.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 0, 0));
        archive2.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 5, 0));
        archive3.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 10, 0));
        archive4.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 15, 0));
        var query = new GetArchiveQuery(member.getId(), memoFolder.getId(), ArchiveTab.MEMO);

        // when
        var archives = archiveService.getArchive(query);

        // then
        assertAll(
                () -> assertThat(archives).hasSize(2),
                () -> assertThat(archives).containsExactly(
                        ArchiveUnitResult.of(archive2),
                        ArchiveUnitResult.of(archive1)
                )
        );
    }

    @Test
    void 참고하는_아카이브_조회() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive1 = archiveRepository.save(끄적이는_아카이브1(member, memoFolder));
        var archive2 = archiveRepository.save(끄적이는_아카이브2(member, memoFolder));
        var archive3 = archiveRepository.save(참고하는_아카이브1(member, memoFolder, 보고하기()));
        var archive4 = archiveRepository.save(참고하는_아카이브2(member, memoFolder, 감사전하기()));
        archive1.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 0, 0));
        archive2.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 5, 0));
        archive3.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 10, 0));
        archive4.setCreatedAtForTest(LocalDateTime.of(2024, 1, 1, 15, 0));
        var query = new GetArchiveQuery(member.getId(), memoFolder.getId(), ArchiveTab.TEMPLATE);

        // when
        var archives = archiveService.getArchive(query);

        // then
        assertAll(
                () -> assertThat(archives).hasSize(2),
                () -> assertThat(archives).containsExactly(
                        ArchiveUnitResult.of(archive4),
                        ArchiveUnitResult.of(archive3)
                )
        );
    }

    @Test
    void 저장한_아카이브가_없을_경우_조회() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var query = new GetArchiveQuery(member.getId(), memoFolder.getId(), ArchiveTab.ALL);

        // when
        var archives = archiveService.getArchive(query);

        // then
        assertThat(archives).hasSize(0);
    }

    @Test
    void 아카이브_수정() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive = archiveRepository.save(끄적이는_아카이브1(member, memoFolder));
        var newContent = "수정된 내용";
        var command = new ModifyArchiveCommand(member.getId(), archive.getId(), newContent);

        // when
        archiveService.modifyArchive(command);

        // then
        assertThat(archive.getContent()).isEqualTo(MemoContent.from(newContent));
    }

    @Test
    void 아카이브_수정시_소유자가_아닌경우_예외를_반환한다() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로1"));
        var memberB = memberRepository.save(MemberFixture.memberWithNickname("바로2"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive = archiveRepository.save(끄적이는_아카이브1(member, memoFolder));
        var newContent = "수정된 내용";
        var command = new ModifyArchiveCommand(memberB.getId(), archive.getId(), newContent);

        // when & then
        assertThatThrownBy(() -> archiveService.modifyArchive(command))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 아카이브_수정시_존재하지_않는_아카이브를_수정하려는_경우_예외를_반환한다() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive = archiveRepository.save(끄적이는_아카이브1(member, memoFolder));
        var invalidArchiveId = 999L;
        var command = new ModifyArchiveCommand(member.getId(), invalidArchiveId, "newContent");

        // when & then
        assertThatThrownBy(() -> archiveService.modifyArchive(command))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.NOT_EXIST_ARCHIVE);
    }

    @Test
    void 아카이브_수정시_500자를_초과할경우_예외를_반환한다() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive = archiveRepository.save(끄적이는_아카이브1(member, memoFolder));
        var newContent = "가".repeat(501);
        var command = new ModifyArchiveCommand(member.getId(), archive.getId(), newContent);

        // when & then
        assertThatThrownBy(() -> archiveService.modifyArchive(command))
                .isInstanceOf(MemoException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoExceptionType.OVER_MAX_SIZE_CONTENT);
    }

    @Test
    void 템플릿_아카이브_수정시_예외를_반환한다() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive = archiveRepository.save(참고하는_아카이브1(member, memoFolder, 감사전하기()));
        var newContent = "가".repeat(501);
        var command = new ModifyArchiveCommand(member.getId(), archive.getId(), newContent);

        // when & then
        assertThatThrownBy(() -> archiveService.modifyArchive(command))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.CANT_MODIFY_TEMPLATE);
    }

    @Test
    void 아카이브를_삭제한다() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive = archiveRepository.save(끄적이는_아카이브1(member, memoFolder));
        var command = new DeleteArchiveCommand(member.getId(), archive.getId());

        // when
        archiveService.deleteArchive(command);

        // then
        assertThat(archiveRepository.findAll()).hasSize(0);
    }

    @Test
    void 아카이브_삭제시_소유자가_아닌경우_예외를_반환한다() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로1"));
        var memberB = memberRepository.save(MemberFixture.memberWithNickname("바로2"));
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive = archiveRepository.save(끄적이는_아카이브1(member, memoFolder));
        var command = new DeleteArchiveCommand(memberB.getId(), archive.getId());

        // when & then
        assertThatThrownBy(() -> archiveService.deleteArchive(command))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 존재하지_않는_아카이브_삭제_요청시_예외를_반환한다() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var invalidArchiveId = 999L;
        var command = new DeleteArchiveCommand(member.getId(), invalidArchiveId);

        // when & then
        assertThatThrownBy(() -> archiveService.deleteArchive(command))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.NOT_EXIST_ARCHIVE);
    }
}
