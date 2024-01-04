package com.baro.member.application;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fake.FakeNicknameCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemberCreatorTest {

    NicknameCreator nicknameCreator = new FakeNicknameCreator();
    MemberRepository memberRepository;
    MemberCreator memberCreator;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        memberCreator = new MemberCreator(memberRepository, nicknameCreator);
    }

    @Test
    void 생성된_닉네임을_가진_멤버를_저장한다() {

        // given
        String name = "name";
        String email = "email";
        String oauthId = "oauthId";
        String oauthType = "kakao";

        String createdNickname = "nickname";

        // when
        memberCreator.create(name, email, oauthId, oauthType);

        // then
        Optional<Member> savedMember = memberRepository.findByOAuthIdAndOAuthServiceType(oauthId, oauthType);
        assertEquals(savedMember.get().getNickname(), createdNickname);
    }

    @Test
    void 생성된_닉네임이_중복되면_다시_생성한다() {

        // mock을 사용..?...
    }
}
