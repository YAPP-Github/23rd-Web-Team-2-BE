package com.baro.memo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.application.dto.SaveTemporalMemoCommand;
import com.baro.memo.application.dto.UpdateTemporalMemoCommand;
import com.baro.memo.domain.MemoContent;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memo.exception.MemoException;
import com.baro.memo.exception.MemoExceptionType;
import com.baro.memo.fake.FakeTemporalMemoRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemoServiceTest {

    private MemoService memoService;
    private MemberRepository memberRepository;
    private TemporalMemoRepository temporalMemoRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        temporalMemoRepository = new FakeTemporalMemoRepository();
        memoService = new MemoService(memberRepository, temporalMemoRepository);
    }

    @Test
    void 끄적이는_메모_저장() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        String content = "testContent";
        SaveTemporalMemoCommand command = new SaveTemporalMemoCommand(memberId, content);

        // when
        memoService.saveTemporalMemo(command);

        // then
        List<TemporalMemo> all = temporalMemoRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getContent().value()).isEqualTo(content);
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
        memoService.updateTemporalMemo(command);

        // then
        TemporalMemo updatedTemporalMemo = temporalMemoRepository.getById(temporalMemoId);
        assertThat(updatedTemporalMemo.getContent()).isEqualTo(MemoContent.from(updateContent));
    }

    @Test
    void 다른_회원의_끄적이는_메모_수정시_오류_발생() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        TemporalMemo temporalMemo = TemporalMemo.of(memberRepository.getById(memberId), "testContent");
        Long temporalMemoId = temporalMemoRepository.save(temporalMemo).getId();

        Long otherMemberId = memberRepository.save(MemberFixture.memberWithNickname("nickname2")).getId();
        String updateContent = "updatedContent";
        UpdateTemporalMemoCommand command = new UpdateTemporalMemoCommand(otherMemberId, temporalMemoId, updateContent);

        // when & then
        assertThatThrownBy(() -> memoService.updateTemporalMemo(command))
                .isInstanceOf(MemoException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoExceptionType.NOT_MATCH_OWNER);
    }

    @Test
    void 끄적이는_메모_수정_내용의_길이_초과시_오류_발생() {
        // given
        Long memberId = memberRepository.save(MemberFixture.memberWithNickname("nickname1")).getId();
        TemporalMemo temporalMemo = TemporalMemo.of(memberRepository.getById(memberId), "testContent");
        Long temporalMemoId = temporalMemoRepository.save(temporalMemo).getId();

        String updateContent = "updatedContent".repeat(500);
        UpdateTemporalMemoCommand command = new UpdateTemporalMemoCommand(memberId, temporalMemoId, updateContent);

        // when & then
        assertThatThrownBy(() -> memoService.updateTemporalMemo(command))
                .isInstanceOf(MemoException.class)
                .extracting("exceptionType")
                .isEqualTo(MemoExceptionType.OVER_MAX_SIZE_CONTENT);
    }
}
