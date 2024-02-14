package com.baro.template.application;

import static com.baro.template.fixture.TemplateFixture.감사전하기;
import static com.baro.template.fixture.TemplateFixture.보고하기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import com.baro.archive.fake.FakeArchiveRepository;
import com.baro.archive.fixture.ArchiveFixture;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.domain.MemoContent;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import com.baro.memofolder.fake.FakeMemoFolderRepository;
import com.baro.template.application.dto.ArchiveTemplateCommand;
import com.baro.template.application.dto.CopyTemplateCommand;
import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
import com.baro.template.application.dto.UnArchiveTemplateCommand;
import com.baro.template.domain.TemplateCategory;
import com.baro.template.domain.TemplateRepository;
import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
import com.baro.template.fake.FakeTemplateRepository;
import com.baro.template.presentation.SortType;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TemplateServiceTest {

    private TemplateService service;
    private TemplateRepository templateRepository;
    private MemberRepository memberRepository;
    private MemoFolderRepository memoFolderRepository;
    private ArchiveRepository archiveRepository;

    @BeforeEach
    void setUp() {
        templateRepository = new FakeTemplateRepository();
        memberRepository = new FakeMemberRepository();
        memoFolderRepository = new FakeMemoFolderRepository();
        archiveRepository = new FakeArchiveRepository();
        service = new TemplateService(templateRepository, memberRepository, memoFolderRepository, archiveRepository);
    }

    @Test
    void 최신순_템플릿_조회() {
        // given
        templateRepository.save(보고하기());
        templateRepository.save(보고하기());
        templateRepository.save(보고하기());
        templateRepository.save(감사전하기());
        var memberId = 1L;

        // when
        var result = service.findTemplates(
                new FindTemplateQuery(memberId, TemplateCategory.REPORT, SortType.NEW.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(3),
                () -> assertThat(result.getContent()).isSortedAccordingTo(
                        Comparator.comparing(FindTemplateResult::templateId).reversed())
        );
    }

    @Test
    void 복사순_템플릿_조회() {
        // given
        templateRepository.save(보고하기(0, 0));
        templateRepository.save(보고하기(0, 1));
        templateRepository.save(보고하기(0, 2));
        templateRepository.save(감사전하기(0, 3));
        var memberId = 1L;

        // when
        var result = service.findTemplates(
                new FindTemplateQuery(memberId, TemplateCategory.REPORT, SortType.COPY.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getNumberOfElements()).isEqualTo(3),
                () -> assertThat(result.getContent()).isSortedAccordingTo(
                        Comparator.comparing(FindTemplateResult::copiedCount).reversed())
        );
    }

    @Test
    void 저장순_템플릿_조회() {
        // given
        templateRepository.save(보고하기(0, 0));
        templateRepository.save(보고하기(1, 0));
        templateRepository.save(보고하기(2, 0));
        templateRepository.save(감사전하기(3, 0));
        var memberId = 1L;

        // when
        var result = service.findTemplates(
                new FindTemplateQuery(memberId, TemplateCategory.REPORT, SortType.SAVE.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getNumberOfElements()).isEqualTo(3),
                () -> assertThat(result.getContent()).isSortedAccordingTo(
                        Comparator.comparing(FindTemplateResult::savedCount).reversed())
        );
    }

    @Test
    void 빈_템플릿_조회() {
        // when
        var memberId = 1L;
        var result = service.findTemplates(
                new FindTemplateQuery(memberId, TemplateCategory.REPORT, SortType.NEW.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getContent()).isEmpty(),
                () -> assertThat(result.getNumberOfElements()).isEqualTo(0),
                () -> assertThat(result.isFirst()).isEqualTo(true),
                () -> assertThat(result.isLast()).isEqualTo(true)
        );
    }

    @Test
    void 템플릿_조회시_아카이브_여부_표시() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기(0, 0));
        templateRepository.save(보고하기(0, 1));
        templateRepository.save(보고하기(0, 2));
        templateRepository.save(감사전하기(0, 3));
        archiveRepository.save(ArchiveFixture.참고하는_아카이브1(member, MemoFolder.defaultFolder(member), template));

        // when
        var result = service.findTemplates(
                new FindTemplateQuery(member.getId(), TemplateCategory.REPORT, SortType.COPY.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getNumberOfElements()).isEqualTo(3),
                () -> assertThat(result.getContent()).isSortedAccordingTo(
                        Comparator.comparing(FindTemplateResult::copiedCount).reversed()),
                () -> assertThat(result.getContent().get(0).isArchived()).isTrue()
        );
    }

    @Test
    void 템플릿_복사() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var countBeforeCopy = template.getCopiedCount();
        var command = new CopyTemplateCommand(member.getId(), template.getId());

        // when
        service.copyTemplate(command);

        // then
        assertThat(template.getCopiedCount()).isEqualTo(countBeforeCopy + 1);
    }

    @Test
    void 존재하지_않는_템플릿_복사시_예외_발생() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var invalidTemplateId = 9999L;
        var command = new CopyTemplateCommand(member.getId(), invalidTemplateId);

        // when & then
        assertThatThrownBy(() -> service.copyTemplate(command))
                .isInstanceOf(TemplateException.class)
                .extracting("exceptionType")
                .isEqualTo(TemplateExceptionType.INVALID_TEMPLATE);
    }

    @Test
    void 템플릿_아카이브() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var command = new ArchiveTemplateCommand(member.getId(), template.getId(), memoFolder.getId());

        // when
        service.archiveTemplate(command);

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
        service.archiveTemplate(command);

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
        assertThatThrownBy(() -> service.archiveTemplate(command))
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
        assertThatThrownBy(() -> service.archiveTemplate(command))
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
        assertThatThrownBy(() -> service.archiveTemplate(command))
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
        service.archiveTemplate(command);

        // when & then
        assertThatThrownBy(() -> service.archiveTemplate(command))
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
        assertThatThrownBy(() -> service.archiveTemplate(command))
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
        service.unarchiveTemplate(command);

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
        service.unarchiveTemplate(command);

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
        assertThatThrownBy(() -> service.unarchiveTemplate(command))
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
        assertThatThrownBy(() -> service.unarchiveTemplate(command))
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
        assertThatThrownBy(() -> service.unarchiveTemplate(command))
                .isInstanceOf(ArchiveException.class)
                .extracting("exceptionType")
                .isEqualTo(ArchiveExceptionType.NOT_ARCHIVED_TEMPLATE);
    }
}
