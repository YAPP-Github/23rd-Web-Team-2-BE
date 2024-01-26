package com.baro.member.application;

import com.baro.member.application.dto.GetMemberProfileResult;
import com.baro.member.application.dto.UpdateMemberProfileCommand;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
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

    public void updateProfile(UpdateMemberProfileCommand command) {
        Member member = memberRepository.getById(command.id());
        validateDuplicatedNickname(command.nickname());
        member.updateProfile(command.name(), command.nickname());
    }

    private void validateDuplicatedNickname(String nickName) {
        if (memberRepository.existByNickname(nickName)) {
            throw new MemberException(MemberExceptionType.NICKNAME_DUPLICATION);
        }
    }
}
