package com.baro.member.infrastructure;

import com.baro.member.domain.MemberWithdrawalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWithdrawalInfoJpaRepository extends JpaRepository<MemberWithdrawalInfo, Long> {
}
