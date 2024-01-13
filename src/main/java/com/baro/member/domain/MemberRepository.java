package com.baro.member.domain;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findByOAuthIdAndOAuthServiceType(String oAuthId, String oAuthServiceType);

    Member save(Member member);

    List<Member> findAll();

    boolean existByNickname(String nickname);

    Optional<Member> findById(Long memberId);
}
