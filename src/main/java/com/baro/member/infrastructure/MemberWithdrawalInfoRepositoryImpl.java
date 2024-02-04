package com.baro.member.infrastructure;

import com.baro.member.domain.MemberWithdrawalInfo;
import com.baro.member.domain.MemberWithdrawalInfoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberWithdrawalInfoRepositoryImpl implements MemberWithdrawalInfoRepository {

    private final MemberWithdrawalInfoJpaRepository memberWithdrawalInfoJpaRepository;

    @Override
    public MemberWithdrawalInfo save(MemberWithdrawalInfo memberWithdrawalInfo) {
        return memberWithdrawalInfoJpaRepository.save(memberWithdrawalInfo);
    }
}
