package com.baro.memo.application;


import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memo.application.dto.SaveTemporalMemoCommand;
import com.baro.memo.application.dto.SaveTemporalMemoResult;
import com.baro.memo.application.dto.UpdateTemporalMemoCommand;
import com.baro.memo.domain.MemoContent;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemoService {

    private final MemberRepository memberRepository;
    private final TemporalMemoRepository temporalMemoRepository;

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
}
