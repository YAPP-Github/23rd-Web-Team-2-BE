package com.baro.template.application;

import static com.baro.template.fixture.TemplateFixture.감사전하기;
import static com.baro.template.fixture.TemplateFixture.보고하기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import com.baro.memofolder.fake.FakeMemoFolderRepository;
import com.baro.template.application.dto.ArchiveTemplateCommand;
import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
import com.baro.template.application.dto.UnArchiveTemplateCommand;
import com.baro.template.domain.TemplateCategory;
import com.baro.template.domain.TemplateMember;
import com.baro.template.domain.TemplateMemberRepository;
import com.baro.template.domain.TemplateRepository;
import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
import com.baro.template.fake.FakeTemplateMemberRepository;
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
    private TemplateMemberRepository templateMemberRepository;

    @BeforeEach
    void setUp() {
        templateRepository = new FakeTemplateRepository();
        memberRepository = new FakeMemberRepository();
        memoFolderRepository = new FakeMemoFolderRepository();
        templateMemberRepository = new FakeTemplateMemberRepository();
        service = new TemplateService(templateRepository, memberRepository, memoFolderRepository,
                templateMemberRepository);
    }

    @Test
    void 최신순_템플릿_조회() {
        // given
        templateRepository.save(보고하기());
        templateRepository.save(보고하기());
        templateRepository.save(보고하기());
        templateRepository.save(감사전하기());

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.NEW.getSort()));

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

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.COPY.getSort()));

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

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.SAVE.getSort()));

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
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.NEW.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getContent()).isEmpty(),
                () -> assertThat(result.getNumberOfElements()).isEqualTo(0),
                () -> assertThat(result.isFirst()).isEqualTo(true),
                () -> assertThat(result.isLast()).isEqualTo(true)
        );
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
        var templateMembers = templateMemberRepository.findAll();
        var savedTemplateMember = templateMembers.get(0);
        assertAll(
                () -> assertThat(templateMembers).hasSize(1),
                () -> assertThat(savedTemplateMember.getMember()).isEqualTo(member),
                () -> assertThat(savedTemplateMember.getTemplate()).isEqualTo(template),
                () -> assertThat(savedTemplateMember.getMemoFolder()).isEqualTo(memoFolder)
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
                .isInstanceOf(TemplateException.class)
                .extracting("exceptionType")
                .isEqualTo(TemplateExceptionType.ARCHIVED_TEMPLATE);
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
        var templateMember = TemplateMember.instanceForTest(1L, member, memoFolder, template);
        templateMemberRepository.save(templateMember);
        var command = new UnArchiveTemplateCommand(member.getId(), template.getId());

        // when
        service.unarchiveTemplate(command);

        // then
        var templateMembers = templateMemberRepository.findAll();
        assertThat(templateMembers).hasSize(0);
    }

    @Test
    void 템플릿_아카이브_취소시_템플릿_저장횟수_감소() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var memoFolder = memoFolderRepository.save(MemoFolder.defaultFolder(member));
        var templateMember = TemplateMember.instanceForTest(1L, member, memoFolder, template);
        templateMemberRepository.save(templateMember);
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
                .isInstanceOf(TemplateException.class)
                .extracting("exceptionType")
                .isEqualTo(TemplateExceptionType.NOT_ARCHIVED_TEMPLATE);
    }
}
