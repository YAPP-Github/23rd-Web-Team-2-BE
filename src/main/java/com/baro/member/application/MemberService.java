package com.baro.member.application;

import com.baro.archive.domain.ArchiveRepository;
import com.baro.common.image.ImageStorageClient;
import com.baro.common.image.dto.ImageUploadResult;
import com.baro.member.application.dto.DeleteMemberCommand;
import com.baro.member.application.dto.GetMemberProfileResult;
import com.baro.member.application.dto.UpdateMemberProfileCommand;
import com.baro.member.application.dto.UpdateProfileImageCommand;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.domain.MemberWithdrawalInfo;
import com.baro.member.domain.MemberWithdrawalInfoRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memofolder.domain.MemoFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ImageStorageClient imageStorageClient;
    private final TemporalMemoRepository temporalMemoRepository;
    private final ArchiveRepository archiveRepository;
    private final MemoFolderRepository memoFolderRepository;
    private final MemberWithdrawalInfoRepository memberWithdrawalInfoRepository;

    @Transactional(readOnly = true)
    public GetMemberProfileResult getMyProfile(Long id) {
        Member member = memberRepository.getById(id);
        return GetMemberProfileResult.from(member);
    }

    public void updateProfile(UpdateMemberProfileCommand command) {
        Member member = memberRepository.getById(command.id());
        if (member.isOtherNickname(command.nickname())) {
            validateDuplicatedNickname(command.nickname());
        }
        member.updateProfile(command.name(), command.nickname());
    }

    private void validateDuplicatedNickname(String nickName) {
        if (memberRepository.existByNickname(nickName)) {
            throw new MemberException(MemberExceptionType.NICKNAME_DUPLICATION);
        }
    }

    public void deleteProfileImage(Long id) {
        Member member = memberRepository.getById(id);
        String profileImageKey = member.getProfileImageUrl();
        member.deleteProfileImage();
        imageStorageClient.delete(profileImageKey);
    }

    public void updateProfileImage(UpdateProfileImageCommand command) {
        Member member = memberRepository.getById(command.id());
        if (!member.isDefaultImage()) {
            imageStorageClient.delete(member.getProfileImageUrl());
        }
        ImageUploadResult imageUploadResult = imageStorageClient.upload(command.image());
        member.updateProfileImage(imageUploadResult.key());
    }

    public void deleteMember(DeleteMemberCommand command) {
        Member member = memberRepository.getById(command.memberId());
        temporalMemoRepository.deleteAllByMember(member);
        archiveRepository.deleteAllByMember(member);
        memoFolderRepository.deleteAllByMember(member);
        memberRepository.deleteById(command.memberId());
        memberWithdrawalInfoRepository.save(new MemberWithdrawalInfo(command.reason()));
    }
}
