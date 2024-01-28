package com.baro.member.infrastructure;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberNickname;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findByOAuthIdAndOAuthServiceType(String oAuthId, String oAuthServiceType) {
        return memberJpaRepository.findByOAuthIdAndOAuthServiceType(oAuthId, oAuthServiceType);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll();
    }

    @Override
    public boolean existByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(MemberNickname.from(nickname));
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }

    @Override
    public Member getById(Long memberId) {
        return this.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_EXIST_MEMBER));
    }
}
