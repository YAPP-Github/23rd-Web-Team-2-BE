package com.baro.member.infrastructure;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * p5. 패턴을 보아하니 Repository, RepositoryImpl 를 명시적으로 분리하고 있는 것으로 보여요.
 * 일종의 Adapter 패턴으로 보이고, 이를 추상화 한 것으로 추측 되는데
 * 개인적으론 효용성이 얼마나 있을지가 의문이 드네요.
 *
 * Clean 아키텍쳐나 헥사고날 아키텍쳐가 이런 방식을 텍하고 있는걸로 아는데
 * 지금 사용하시는 아키텍쳐는 Layered로 보이고, 굳이 JpaRepository 직접 사용을 피하는건 너무 번거로운 작업이 아닌가 싶습니다.
 */
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
}
