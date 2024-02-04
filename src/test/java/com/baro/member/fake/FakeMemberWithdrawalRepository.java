package com.baro.member.fake;

import com.baro.member.domain.MemberWithdrawalInfo;
import com.baro.member.domain.MemberWithdrawalInfoRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberWithdrawalRepository implements MemberWithdrawalInfoRepository {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, MemberWithdrawalInfo> memberWithdrawalInfos = new ConcurrentHashMap<>();

    @Override
    public MemberWithdrawalInfo save(MemberWithdrawalInfo memberWithdrawalInfo) {
        if (memberWithdrawalInfo.getId() == null) {
            MemberWithdrawalInfo newMemberWithdrawalInfo = new MemberWithdrawalInfo(
                    id.getAndIncrement(),
                    memberWithdrawalInfo.getReason()
            );
            memberWithdrawalInfos.put(newMemberWithdrawalInfo.getId(), newMemberWithdrawalInfo);
            return newMemberWithdrawalInfo;
        }
        memberWithdrawalInfos.put(memberWithdrawalInfo.getId(), memberWithdrawalInfo);
        return memberWithdrawalInfo;
    }
}
