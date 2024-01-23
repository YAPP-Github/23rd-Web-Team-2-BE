package com.baro.memo.batch;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.member.domain.Member;
import com.baro.member.fixture.MemberFixture;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memo.fake.FakeTemporalMemoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TemporalMemoSchedulerTest {

    private static final LocalDateTime _7DAYS_BEFORE = LocalDate.now().minusDays(7L).atStartOfDay();
    private static final LocalDateTime _3DAYS_BEFORE = LocalDate.now().minusDays(3L).atStartOfDay();

    private TemporalMemoScheduler temporalMemoScheduler;
    private TemporalMemoRepository temporalMemoRepository;

    @BeforeEach
    void setUp() {
        temporalMemoRepository = new FakeTemporalMemoRepository();
        temporalMemoScheduler = new TemporalMemoScheduler(temporalMemoRepository);
    }

    @Test
    void 일주일이_지난_끄적이는_메모를_삭제한다() {
        // given
        Member member = MemberFixture.memberWithNickname("baro");
        TemporalMemo temporalMemoA = temporalMemoRepository.save(TemporalMemo.of(member, "끄적이는 메모A"));
        TemporalMemo temporalMemoB = temporalMemoRepository.save(TemporalMemo.of(member, "끄적이는 메모B"));
        TemporalMemo temporalMemoC = temporalMemoRepository.save(TemporalMemo.of(member, "끄적이는 메모C"));
        temporalMemoA.setCreatedAtForTest(_7DAYS_BEFORE);
        temporalMemoB.setCreatedAtForTest(_7DAYS_BEFORE);
        temporalMemoC.setCreatedAtForTest(_3DAYS_BEFORE);

        // when
        temporalMemoScheduler.deleteTemporalMemo();

        // then
        List<TemporalMemo> all = temporalMemoRepository.findAll();
        assertThat(all).containsExactly(temporalMemoC);
    }
}
