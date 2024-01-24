package com.baro.archive.application;

import static com.baro.template.fixture.TemplateFixture.보고하기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import com.baro.archive.fake.FakeArchiveRepository;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.application.dto.ArchiveTemporalMemoCommand;
import com.baro.memo.domain.MemoContent;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memo.exception.TemporalMemoException;
import com.baro.memo.exception.TemporalMemoExceptionType;
import com.baro.memo.fake.FakeTemporalMemoRepository;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import com.baro.memofolder.fake.FakeMemoFolderRepository;
import com.baro.template.application.dto.ArchiveTemplateCommand;
import com.baro.template.application.dto.UnArchiveTemplateCommand;
import com.baro.template.domain.TemplateRepository;
import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
import com.baro.template.fake.FakeTemplateRepository;
import java.util.List;
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
    private TemporalMemoRepository temporalMemoRepository;
    private MemoFolderRepository memoFolderRepository;
    private TemplateRepository templateRepository;

    @BeforeEach
    void setUp() {
        archiveRepository = new FakeArchiveRepository();
        memberRepository = new FakeMemberRepository();
        temporalMemoRepository = new FakeTemporalMemoRepository();
        memoFolderRepository = new FakeMemoFolderRepository();
        templateRepository = new FakeTemplateRepository();
        archiveService = new ArchiveService(archiveRepository, memberRepository, memoFolderRepository,
                temporalMemoRepository, templateRepository);
    }

    @Test
    void 끄적이는_메모_아카이브() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        TemporalMemo temporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, "testContent"));
        Long memoFolderId = memoFolderRepository.save(MemoFolder.defaultFolder(member)).getId();

        ArchiveTemporalMemoCommand command = new ArchiveTemporalMemoCommand(member.getId(), temporalMemo.getId(),
                memoFolderId);

        // when
        archiveService.archiveTemporalMemo(command);

        // then
        List<Archive> archives = archiveRepository.findAll();
        Archive archive = archives.get(0);
        assertAll(
                () -> assertThat(archives).hasSize(1),
                () -> assertThat(archive.getContent()).isEqualTo(temporalMemo.getArchivingContent()),
                () -> assertThat(temporalMemo.getArchive()).isEqualTo(archive)
        );
    }

    @Test
    void 다른_사람의_끄적이는_메모_아카이브시_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        Member otherMember = memberRepository.save(MemberFixture.memberWithNickname("nickname2"));
        TemporalMemo temporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, "testContent"));
        Long memoFolderId = memoFolderRepository.save(MemoFolder.defaultFolder(otherMember)).getId();

        ArchiveTemporalMemoCommand command = new ArchiveTemporalMemoCommand(otherMember.getId(), temporalMemo.getId(),
                memoFolderId);

        // when & then
        assertThatThrownBy(() -> archiveService.archiveTemporalMemo(command))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 존재하지_않는_메모_폴더에_아카이브시_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        TemporalMemo temporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, "testContent"));
        Long notExistMemoFolderId = 1L;

        ArchiveTemporalMemoCommand command = new ArchiveTemporalMemoCommand(member.getId(), temporalMemo.getId(),
                notExistMemoFolderId);

        // when & then
        assertThatThrownBy(() -> archiveService.archiveTemporalMemo(command))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.NOT_EXIST_MEMO_FOLDER);
    }

    @Test
    void 다른_사람의_메모_폴더에_아카이브시_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        Member otherMember = memberRepository.save(MemberFixture.memberWithNickname("nickname2"));
        TemporalMemo temporalMemo = temporalMemoRepository.save(TemporalMemo.of(otherMember, "testContent"));
        Long memoFolderId = memoFolderRepository.save(MemoFolder.defaultFolder(member)).getId();

        ArchiveTemporalMemoCommand command = new ArchiveTemporalMemoCommand(otherMember.getId(), temporalMemo.getId(),
                memoFolderId);

        // when & then
        assertThatThrownBy(() -> archiveService.archiveTemporalMemo(command))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 템플릿_아카이브() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var command = new ArchiveTemplateCommand(member.getId(), template.getId(), memoFolder.getId());

        // when
        archiveService.archiveTemplate(command);

        // then
        var archives = archiveRepository.findAll();
        assertAll(
                () -> assertThat(archives).hasSize(1),
                () -> assertThat(archives.get(0).getMemoFolder()).isEqualTo(memoFolder),
                () -> assertThat(archives.get(0).getMember()).isEqualTo(member),
                () -> assertThat(archives.get(0).getTemplate()).isEqualTo(template)
        );
    }

    @Test
    void 템플릿_아카이브시_템플릿_저장횟수_증가() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var command = new ArchiveTemplateCommand(member.getId(), template.getId(), memoFolder.getId());
        var beforeSavedCount = template.getSavedCount();

        // when
        archiveService.archiveTemplate(command);

        // then
        assertThat(template.getSavedCount()).isEqualTo(beforeSavedCount + 1);
    }

    @Test
    void 존재하지_않는_템플릿_아카이브시_예외_발생() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var invalidTemplateId = 9999L;
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var command = new ArchiveTemplateCommand(member.getId(), invalidTemplateId, memoFolder.getId());

        // when & then
        assertThatThrownBy(() -> archiveService.archiveTemplate(command))
                .isInstanceOf(TemplateException.class)
                .extracting("exceptionType")
                .isEqualTo(TemplateExceptionType.INVALID_TEMPLATE);
    }

    @Test
    void 폴더_주인이_일치하지_않는_경우_예외_발생() {
        // given
        var memberA = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var memberB = memberRepository.save(MemberFixture.memberWithNickname("바로바로론"));
        var template = templateRepository.save(보고하기());
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(memberB));
        var command = new ArchiveTemplateCommand(memberA.getId(), template.getId(), memoFolder.getId());

        // when & then
        assertThatThrownBy(() -> archiveService.archiveTemplate(command))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 존재하지_않는_멤버가_템플릿_아카이브_시도시_예외_발생() {
        // given
        var invalidMemberId = 9999L;
        var memberA = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(memberA));
        var command = new ArchiveTemplateCommand(invalidMemberId, template.getId(), memoFolder.getId());

        // when & then
        assertThatThrownBy(() -> archiveService.archiveTemplate(command))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.NOT_EXIST_MEMBER);
    }

    @Test
    void 이미_아카이브한_템플릿일_경우_예외_발생() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var command = new ArchiveTemplateCommand(member.getId(), template.getId(), memoFolder.getId());
        archiveService.archiveTemplate(command);

        // when & then
        assertThatThrownBy(() -> archiveService.archiveTemplate(command))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.ARCHIVED_TEMPLATE);
    }

    @Test
    void 존재하지_않는_폴더에_아카이브시_예외_발생() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var invalidMemoFolderId = 9999L;
        var command = new ArchiveTemplateCommand(member.getId(), template.getId(), invalidMemoFolderId);

        // when & then
        assertThatThrownBy(() -> archiveService.archiveTemplate(command))
                .isInstanceOf(MemoFolderException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoFolderExceptionType.NOT_EXIST_MEMO_FOLDER);
    }

    @Test
    void 템플릿_아카이브_취소() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive = new Archive(member, memoFolder, MemoContent.from(template.getContent()), template);
        archiveRepository.save(archive);
        var command = new UnArchiveTemplateCommand(member.getId(), template.getId());

        // when
        archiveService.unarchiveTemplate(command);

        // then
        var archives = archiveRepository.findAll();
        assertThat(archives).hasSize(0);
    }

    @Test
    void 템플릿_아카이브_취소시_템플릿_저장횟수_감소() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var archive = new Archive(member, memoFolder, MemoContent.from(template.getContent()), template);
        archiveRepository.save(archive);
        var saveCount = template.getSavedCount();
        var command = new UnArchiveTemplateCommand(member.getId(), template.getId());

        // when
        archiveService.unarchiveTemplate(command);

        // then
        assertThat(template.getSavedCount()).isEqualTo(saveCount - 1);
    }

    @Test
    void 존재하지_않는_템플릿_아카이브_취소시_예외_발생() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var invalidTemplateId = 9999L;
        var command = new UnArchiveTemplateCommand(member.getId(), invalidTemplateId);

        // when & then
        assertThatThrownBy(() -> archiveService.unarchiveTemplate(command))
                .isInstanceOf(TemplateException.class)
                .extracting("exceptionType")
                .isEqualTo(TemplateExceptionType.INVALID_TEMPLATE);
    }

    @Test
    void 존재하지_않는_멤버가_템플릿_아카이브_취소시도시_예외_발생() {
        // given
        var invalidMemberId = 9999L;
        var template = templateRepository.save(보고하기());
        var command = new UnArchiveTemplateCommand(invalidMemberId, template.getId());

        // when & then
        assertThatThrownBy(() -> archiveService.unarchiveTemplate(command))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.NOT_EXIST_MEMBER);
    }

    @Test
    void 템플릿_아카이브_요청전_아카이브_취소_요청시_예외_발생() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var command = new UnArchiveTemplateCommand(member.getId(), template.getId());

        // when & then
        assertThatThrownBy(() -> archiveService.unarchiveTemplate(command))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.NOT_ARCHIVED_TEMPLATE);
    }
}
