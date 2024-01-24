package com.baro.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.member.application.dto.GetMemberProfileResult;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @Test
    void 사용자_프로필_조회() {
        // given
        Member savedMember = memberRepository.save(MemberFixture.memberWithNickname("바로"));

        // when
        GetMemberProfileResult result = memberService.getMyProfile(savedMember.getId());

        // then
        assertThat(result.nickname()).isEqualTo("바로");
    }

    @Test
    void 존재하지_않는_사용자_조회시_예외_발생() {
        // given
        Long nonExistMemberId = 1L;

        // when & then
        assertThatThrownBy(() -> memberService.getMyProfile(nonExistMemberId))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.NOT_EXIST_MEMBER);
    }
}
