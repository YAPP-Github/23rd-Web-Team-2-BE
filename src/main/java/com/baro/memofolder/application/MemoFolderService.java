package com.baro.memofolder.application;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.memofolder.application.dto.SaveMemoFolderCommand;
import com.baro.memofolder.application.dto.SaveMemoFolderResult;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
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
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_EXIST_MEMBER));
        validateDuplicatedFolderName(member, folderName);

        MemoFolder savedMemoFolder = memoFolderRepository.save(MemoFolder.of(member, folderName));
        return SaveMemoFolderResult.from(savedMemoFolder);
    }

    private void validateDuplicatedFolderName(Member member, String folderName) {
        if (memoFolderRepository.existByMemberAndName(member, folderName)) {
            throw new MemoFolderException(MemoFolderExceptionType.NAME_DUPLICATION);
        }
    }
}
