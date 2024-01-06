package com.baro.member.application;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fake.FakeNicknameCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberCreatorTest {

    NicknameCreator uniqueNicknameCreator;
    NicknameCreator duplicatedNicknameCreator;

    MemberRepository memberRepository;
    MemberCreator memberCreator;

    @BeforeEach
    void setUp() {
        uniqueNicknameCreator = new FakeNicknameCreator(List.of("nickname1", "nickname2"));
        duplicatedNicknameCreator = new FakeNicknameCreator(List.of("nickname1", "nickname1", "nickname3"));
        memberRepository = new FakeMemberRepository();
        memberCreator = new MemberCreator(memberRepository, uniqueNicknameCreator);
    }

    @Test
    void 생성된_닉네임을_가진_멤버를_저장한다() {

        // given
        String name = "name";
        String email = "email";
        String oauthId = "oauthId";
        String oauthType = "kakao";

        String createdNickname = "nickname1";

        // when
        memberCreator.create(name, email, oauthId, oauthType);

        // then
        Optional<Member> savedMember = memberRepository.findByOAuthIdAndOAuthServiceType(oauthId, oauthType);
        assertThat(savedMember.get().getNickname()).isEqualTo(createdNickname);
    }

    @Test
    void 생성된_닉네임이_중복되면_다시_생성한다() {

        // given
        String name = "name";
        String email = "email";
        String oauthId = "oauthId";
        String oauthType = "kakao";
        String createdNickname = "nickname3";

        memberCreator = new MemberCreator(memberRepository, duplicatedNicknameCreator);
        memberCreator.create(name, email, oauthId, oauthType);

        // when
        Member member = memberCreator.create(name, email, oauthId, oauthType);

        // then
        assertThat(member.getNickname()).isEqualTo(createdNickname);
    }
}
