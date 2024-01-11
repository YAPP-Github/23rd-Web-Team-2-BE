package com.baro.memofolder.application;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memofolder.application.dto.SaveMemoFolderCommand;
import com.baro.memofolder.application.dto.SaveMemoFolderResult;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemoFolderService {

    private final MemberRepository memberRepository;
    private final MemoFolderRepository memoFolderRepository;

    public SaveMemoFolderResult saveMemoFolder(SaveMemoFolderCommand command) {
        Long memberId = command.memberId();
        String folderName = command.name();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        validateDuplicatedFolderName(member, folderName);

        MemoFolder savedMemoFolder = memoFolderRepository.save(MemoFolder.of(member, folderName));
        return SaveMemoFolderResult.from(savedMemoFolder);
    }

    private void validateDuplicatedFolderName(Member member, String folderName) {
        if (memoFolderRepository.existByMemberAndName(member, folderName)) {
            throw new IllegalArgumentException("이미 존재하는 폴더명입니다.");
        }
    }
}
