package com.baro.memo.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.application.dto.SaveTemporalMemoCommand;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
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
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }
}
