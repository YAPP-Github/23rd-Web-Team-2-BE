package com.baro.member.infrastructure;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberNickname;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.oAuthId = :oAuthId AND m.oAuthServiceType = :oAuthServiceType")
    Optional<Member> findByOAuthIdAndOAuthServiceType(String oAuthId, String oAuthServiceType);

    boolean existsByNickname(MemberNickname nickname);
}
