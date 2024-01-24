package com.baro.memo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.application.dto.ApplyCorrectionCommand;
import com.baro.memo.application.dto.DeleteTemporalMemoCommand;
import com.baro.memo.application.dto.FindTemporalMemoHistoriesQuery;
import com.baro.memo.application.dto.FindTemporalMemoHistoriesResult;
import com.baro.memo.application.dto.SaveTemporalMemoCommand;
import com.baro.memo.application.dto.UpdateTemporalMemoCommand;
import com.baro.memo.domain.MemoContent;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memo.exception.MemoException;
import com.baro.memo.exception.MemoExceptionType;
import com.baro.memo.exception.TemporalMemoException;
import com.baro.memo.exception.TemporalMemoExceptionType;
import com.baro.memo.fake.FakeTemporalMemoRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TemporalMemoServiceTest {

    private TemporalMemoService temporalMemoService;
    private MemberRepository memberRepository;
    private TemporalMemoRepository temporalMemoRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        temporalMemoRepository = new FakeTemporalMemoRepository();
        temporalMemoService = new TemporalMemoService(temporalMemoRepository, memberRepository);
    }

    @Test
    void 끄적이는_메모_저장() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        String content = "testContent";
        SaveTemporalMemoCommand command = new SaveTemporalMemoCommand(memberId, content);

        // when
        temporalMemoService.saveTemporalMemo(command);

        // then
        List<TemporalMemo> all = temporalMemoRepository.findAll();
        assertAll(
                () -> assertThat(all).hasSize(1),
                () -> assertThat(all.get(0).getContent().value()).isEqualTo(content)
        );
    }

    @Test
    void 끄적이는_메모_수정() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        TemporalMemo temporalMemo = TemporalMemo.of(memberRepository.getById(memberId), "testContent");
        Long temporalMemoId = temporalMemoRepository.save(temporalMemo).getId();

        String updateContent = "updatedContent";
        UpdateTemporalMemoCommand command = new UpdateTemporalMemoCommand(memberId, temporalMemoId, updateContent);

        // when
        temporalMemoService.updateTemporalMemo(command);

        // then
        TemporalMemo updatedTemporalMemo = temporalMemoRepository.getById(temporalMemoId);
        assertThat(updatedTemporalMemo.getContent()).isEqualTo(MemoContent.from(updateContent));
    }

    @Test
    void 다른_회원의_끄적이는_메모_수정시_예외_발생() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        TemporalMemo temporalMemo = TemporalMemo.of(memberRepository.getById(memberId), "testContent");
        Long temporalMemoId = temporalMemoRepository.save(temporalMemo).getId();

        Long otherMemberId = memberRepository.save(MemberFixture.memberWithNickname("nickname2")).getId();
        String updateContent = "updatedContent";
        UpdateTemporalMemoCommand command = new UpdateTemporalMemoCommand(otherMemberId, temporalMemoId, updateContent);

        // when & then
        assertThatThrownBy(() -> temporalMemoService.updateTemporalMemo(command))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 끄적이는_메모_수정_내용의_길이_초과시_예외_발생() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        TemporalMemo temporalMemo = TemporalMemo.of(memberRepository.getById(memberId), "testContent");
        Long temporalMemoId = temporalMemoRepository.save(temporalMemo).getId();

        String updateContent = "글".repeat(501);
        UpdateTemporalMemoCommand command = new UpdateTemporalMemoCommand(memberId, temporalMemoId, updateContent);

        // when & then
        assertThatThrownBy(() -> temporalMemoService.updateTemporalMemo(command))
                .isInstanceOf(MemoException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoExceptionType.OVER_MAX_SIZE_CONTENT);
    }

    @Test
    void 존재_하지_않는_끄적이는_메모_수정시_예외_발생() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        Long notExistTemporalMemoId = 999L;

        String updateContent = "updatedContent";
        UpdateTemporalMemoCommand command = new UpdateTemporalMemoCommand(memberId, notExistTemporalMemoId,
                updateContent);

        // when & then
        assertThatThrownBy(() -> temporalMemoService.updateTemporalMemo(command))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.NOT_EXIST_TEMPORAL_MEMO);
    }

    @Test
    void 끄적이는_메모_삭제() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        TemporalMemo temporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, "testContent"));
        DeleteTemporalMemoCommand command = new DeleteTemporalMemoCommand(member.getId(), temporalMemo.getId());

        // when
        temporalMemoService.deleteTemporalMemo(command);

        // then
        assertThat(temporalMemoRepository.findAll()).isEmpty();
    }

    @Test
    void 다른_사람의_끄적이는_메모_삭제시_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        Member otherMember = memberRepository.save(MemberFixture.memberWithNickname("nickname2"));
        TemporalMemo temporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, "testContent"));
        DeleteTemporalMemoCommand command = new DeleteTemporalMemoCommand(otherMember.getId(), temporalMemo.getId());

        // when & then
        assertThatThrownBy(() -> temporalMemoService.deleteTemporalMemo(command))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 존재하지_않는_끄적이는_메모_삭제시_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        Long notExistTemporalMemoId = 999L;
        DeleteTemporalMemoCommand command = new DeleteTemporalMemoCommand(member.getId(), notExistTemporalMemoId);

        // when & then
        assertThatThrownBy(() -> temporalMemoService.deleteTemporalMemo(command))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.NOT_EXIST_TEMPORAL_MEMO);
    }

    @Test
    void 끄적이는_메모_맞춤법_검사_결과_반영() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        TemporalMemo temporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, "testContent"));
        String correctionContent = "correctedContent";
        ApplyCorrectionCommand command = new ApplyCorrectionCommand(member.getId(), temporalMemo.getId(),
                correctionContent);

        // when
        temporalMemoService.applyCorrection(command);

        // then
        TemporalMemo updatedTemporalMemo = temporalMemoRepository.getById(temporalMemo.getId());
        assertAll(
                () -> assertThat(updatedTemporalMemo.isCorrected()).isTrue(),
                () -> assertThat(updatedTemporalMemo.getCorrectionContent()).isEqualTo(
                        MemoContent.from(correctionContent))
        );
    }

    @Test
    void 다른_사람의_끄적이는_메모_맞춤법_검사_결과_반영시_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        Member otherMember = memberRepository.save(MemberFixture.memberWithNickname("nickname2"));
        TemporalMemo temporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, "testContent"));
        String correctionContent = "correctedContent";
        ApplyCorrectionCommand command = new ApplyCorrectionCommand(otherMember.getId(), temporalMemo.getId(),
                correctionContent);

        // when & then
        assertThatThrownBy(() -> temporalMemoService.applyCorrection(command))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 존재하지_않는_끄적이는_메모_맞춤법_검사_결과_반영시_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        Long notExistTemporalMemoId = 999L;
        String correctionContent = "correctedContent";
        ApplyCorrectionCommand command = new ApplyCorrectionCommand(member.getId(), notExistTemporalMemoId,
                correctionContent);

        // when & then
        assertThatThrownBy(() -> temporalMemoService.applyCorrection(command))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.NOT_EXIST_TEMPORAL_MEMO);
    }

    @Test
    void 이미_맞춤법_검사가_완료된_끄적이는_메모_맞춤법_검사_결과_반영시_예외_발생() {
        // given
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        TemporalMemo temporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, "testContent"));
        temporalMemo.applyCorrection(MemoContent.from("correctedContent"));
        String correctionContent = "correctedContent";
        ApplyCorrectionCommand command = new ApplyCorrectionCommand(member.getId(), temporalMemo.getId(),
                correctionContent);

        // when & then
        assertThatThrownBy(() -> temporalMemoService.applyCorrection(command))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.ALREADY_CORRECTED);
    }

    @Test
    void 끄적이는_메모_조회시_최신_순으로_정렬() {
        // given
        LocalDate standardDate = LocalDate.now();
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        TemporalMemo temporalMemoA = TemporalMemo.of(member, "testContent1");
        TemporalMemo temporalMemoB = TemporalMemo.of(member, "testContent2");
        TemporalMemo temporalMemoC = TemporalMemo.of(member, "testContent3");
        temporalMemoA.setCreatedAtForTest(standardDate.plusDays(3).atStartOfDay());
        temporalMemoB.setCreatedAtForTest(standardDate.plusDays(3).atStartOfDay());
        temporalMemoC.setCreatedAtForTest(standardDate.plusDays(1).atStartOfDay());
        temporalMemoRepository.save(temporalMemoA);
        temporalMemoRepository.save(temporalMemoB);
        temporalMemoRepository.save(temporalMemoC);

        FindTemporalMemoHistoriesQuery query = new FindTemporalMemoHistoriesQuery(member.getId(),
                standardDate, standardDate.plusDays(4)
        );

        // when
        List<FindTemporalMemoHistoriesResult> temporalMemos = temporalMemoService.findTemporalMemos(query);

        // then
        assertAll(
                () -> assertThat(temporalMemos).hasSize(2),
                () -> assertThat(temporalMemos).isSortedAccordingTo(
                        Comparator.comparing(FindTemporalMemoHistoriesResult::createdAt).reversed()),
                () -> assertThat(temporalMemos.get(0).createdAt()).isEqualTo(standardDate.plusDays(3)),
                () -> assertThat(temporalMemos.get(1).createdAt()).isEqualTo(standardDate.plusDays(1)),
                () -> assertThat(temporalMemos.get(0).temporalMemos()).hasSize(2),
                () -> assertThat(temporalMemos.get(1).temporalMemos()).hasSize(1)
        );
    }

    @Test
    void 끄적이는_메모_조회시_조회_기간_내_끄적이는_메모만_조회_된다() {
        // given
        LocalDate standardDate = LocalDate.now();
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        TemporalMemo temporalMemoA = TemporalMemo.of(member, "testContent1");
        TemporalMemo temporalMemoB = TemporalMemo.of(member, "testContent2");
        TemporalMemo temporalMemoC = TemporalMemo.of(member, "testContent3");
        temporalMemoA.setCreatedAtForTest(standardDate.plusDays(3).atStartOfDay());
        temporalMemoB.setCreatedAtForTest(standardDate.plusDays(3).atStartOfDay());
        temporalMemoC.setCreatedAtForTest(standardDate.plusDays(1).atStartOfDay());
        temporalMemoRepository.save(temporalMemoA);
        temporalMemoRepository.save(temporalMemoB);
        temporalMemoRepository.save(temporalMemoC);

        FindTemporalMemoHistoriesQuery query = new FindTemporalMemoHistoriesQuery(member.getId(),
                standardDate.plusDays(1), standardDate.plusDays(2)
        );

        // when
        List<FindTemporalMemoHistoriesResult> temporalMemos = temporalMemoService.findTemporalMemos(query);

        // then
        assertThat(temporalMemos).hasSize(1);
    }

    @Test
    void 끄적이는_메모_조회시_조회_시작_기간이_끝_기간_보다_이후일_경우_예외_발생() {
        // given
        LocalDate standardDate = LocalDate.now();
        Member member = memberRepository.save(MemberFixture.memberWithNickname("nickname1"));
        TemporalMemo temporalMemoA = TemporalMemo.of(member, "testContent1");
        temporalMemoRepository.save(temporalMemoA);

        FindTemporalMemoHistoriesQuery query = new FindTemporalMemoHistoriesQuery(member.getId(),
                standardDate.plusDays(3), standardDate.plusDays(1)
        );

        // when & then
        assertThatThrownBy(() -> temporalMemoService.findTemporalMemos(query))
                .isInstanceOf(TemporalMemoException.class)
                .extracting("exceptionType")
                .isEqualTo(TemporalMemoExceptionType.NON_SEQUENTIAL_DATES_EXCEPTION);
    }
}
