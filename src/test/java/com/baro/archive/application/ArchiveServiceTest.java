package com.baro.archive.application;

import static com.baro.archive.fixture.ArchiveFixture.끄적이는_아카이브1;
import static com.baro.archive.fixture.ArchiveFixture.끄적이는_아카이브2;
import static com.baro.archive.fixture.ArchiveFixture.참고하는_아카이브1;
import static com.baro.archive.fixture.ArchiveFixture.참고하는_아카이브2;
import static com.baro.template.fixture.TemplateFixture.감사전하기;
import static com.baro.template.fixture.TemplateFixture.보고하기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.archive.application.dto.ArchiveUnitResult;
import com.baro.archive.application.dto.GetArchiveQuery;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.domain.ArchiveTab;
import com.baro.archive.fake.FakeArchiveRepository;
import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
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
}
