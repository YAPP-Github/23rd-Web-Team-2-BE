package com.baro.member.application;

import com.baro.member.application.dto.GetMemberProfileResult;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public GetMemberProfileResult getMyProfile(Long id) {
        Member member = memberRepository.getById(id);
        return GetMemberProfileResult.from(member);
    }
}
