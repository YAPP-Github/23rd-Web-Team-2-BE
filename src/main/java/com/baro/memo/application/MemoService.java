package com.baro.memo.application;


import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memo.application.dto.ArchiveTemporalMemoCommand;
import com.baro.memo.application.dto.ArchiveTemporalMemoResult;
import com.baro.memo.application.dto.SaveTemporalMemoCommand;
import com.baro.memo.application.dto.SaveTemporalMemoResult;
import com.baro.memo.application.dto.UpdateTemporalMemoCommand;
import com.baro.memo.domain.Memo;
import com.baro.memo.domain.MemoContent;
import com.baro.memo.domain.MemoRepository;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final TemporalMemoRepository temporalMemoRepository;
    private final MemberRepository memberRepository;
    private final MemoFolderRepository memoFolderRepository;

    public SaveTemporalMemoResult saveTemporalMemo(SaveTemporalMemoCommand command) {
        Member member = memberRepository.getById(command.memberId());
        TemporalMemo savedTemporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, command.content()));
        return SaveTemporalMemoResult.from(savedTemporalMemo);
    }

    public void updateTemporalMemo(UpdateTemporalMemoCommand command) {
        Member member = memberRepository.getById(command.memberId());
        TemporalMemo temporalMemo = temporalMemoRepository.getById(command.temporalMemoId());
        temporalMemo.matchOwner(member);
        temporalMemo.updateContent(MemoContent.from(command.content()));
    }

    public ArchiveTemporalMemoResult archiveTemporalMemo(ArchiveTemporalMemoCommand command) {
        Member member = memberRepository.getById(command.memberId());
        TemporalMemo temporalMemo = temporalMemoRepository.getById(command.temporalMemoId());
        temporalMemo.matchOwner(member);
        MemoFolder memoFolder = memoFolderRepository.getById(command.memoFolderId());
        memoFolder.matchOwner(member);

        Memo memo = memoRepository.save(Memo.of(member, memoFolder, temporalMemo.getArchivingContent()));
        temporalMemo.archivedAsMemo(memo);
        return ArchiveTemporalMemoResult.from(memo);
    }
}
