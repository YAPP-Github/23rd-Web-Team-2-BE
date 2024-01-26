package com.baro.memofolder.application;

import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memofolder.application.dto.DeleteMemoFolderCommand;
import com.baro.memofolder.application.dto.GetMemoFolderResult;
import com.baro.memofolder.application.dto.RenameMemoFolderCommand;
import com.baro.memofolder.application.dto.SaveMemoFolderCommand;
import com.baro.memofolder.application.dto.SaveMemoFolderResult;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemoFolderService {

    private final MemberRepository memberRepository;
    private final MemoFolderRepository memoFolderRepository;
    private final ArchiveRepository archiveRepository;

    public SaveMemoFolderResult saveMemoFolder(SaveMemoFolderCommand command) {
        Long memberId = command.memberId();
        String folderName = command.name();
        Member member = memberRepository.getById(memberId);
        validateDuplicatedFolderName(member, folderName);

        MemoFolder savedMemoFolder = memoFolderRepository.save(MemoFolder.of(member, folderName));
        return SaveMemoFolderResult.from(savedMemoFolder);
    }

    private void validateDuplicatedFolderName(Member member, String folderName) {
        if (memoFolderRepository.existByMemberAndName(member, folderName)) {
            throw new MemoFolderException(MemoFolderExceptionType.NAME_DUPLICATION);
        }
    }

    public List<GetMemoFolderResult> getMemoFolder(Long memberId) {
        Member member = memberRepository.getById(memberId);
        List<MemoFolder> memoFolders = memoFolderRepository.findAllByMember(member);
        return memoFolders.stream()
                .map(GetMemoFolderResult::from)
                .toList();
    }

    public void renameMemoFolder(RenameMemoFolderCommand command) {
        MemoFolder memoFolder = memoFolderRepository.getById(command.memoFolderId());
        memoFolder.matchOwner(command.memberId());
        validateDuplicatedFolderName(memoFolder.getMember(), command.folderName());

        memoFolder.rename(command.folderName());
    }

    public void deleteMemoFolder(DeleteMemoFolderCommand command) {
        MemoFolder memoFolder = memoFolderRepository.getById(command.memoFolderId());
        memoFolder.matchOwner(command.memberId());
        memoFolder.isNotDefaultFolder();

        if (command.deleteAllMemo()) {
            deleteAllMemoInFolder(command.memberId(), memoFolder);
            return;
        }
        replaceAllMemoToDefaultFolder(command.memberId(), memoFolder);
    }

    private void deleteAllMemoInFolder(Long memberId, MemoFolder memoFolder) {
        archiveRepository.deleteAllByMemberIdAndMemoFolderId(memberId, memoFolder.getId());
        memoFolderRepository.delete(memoFolder);
    }

    private void replaceAllMemoToDefaultFolder(Long memberId, MemoFolder memoFolder) {
        MemoFolder defaultFolder = memoFolderRepository.getByMemberIdAndIsDefaultTrue(memberId);
        List<Archive> archives = archiveRepository.findAllByMemberIdAndMemoFolderId(memberId, memoFolder.getId());
        archives.forEach(archive -> archive.changeMemoFolder(defaultFolder));
        memoFolderRepository.delete(memoFolder);
    }
}
