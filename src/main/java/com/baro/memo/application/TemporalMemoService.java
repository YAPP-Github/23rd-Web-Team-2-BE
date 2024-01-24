package com.baro.memo.application;


import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memo.application.dto.ApplyCorrectionCommand;
import com.baro.memo.application.dto.DeleteTemporalMemoCommand;
import com.baro.memo.application.dto.FindTemporalMemoHistoriesQuery;
import com.baro.memo.application.dto.FindTemporalMemoHistoriesResult;
import com.baro.memo.application.dto.FindTemporalMemoResult;
import com.baro.memo.application.dto.SaveTemporalMemoCommand;
import com.baro.memo.application.dto.SaveTemporalMemoResult;
import com.baro.memo.application.dto.UpdateTemporalMemoCommand;
import com.baro.memo.domain.MemoContent;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memo.exception.TemporalMemoException;
import com.baro.memo.exception.TemporalMemoExceptionType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TemporalMemoService {

    private final TemporalMemoRepository temporalMemoRepository;
    private final MemberRepository memberRepository;

    public SaveTemporalMemoResult saveTemporalMemo(SaveTemporalMemoCommand command) {
        Member member = memberRepository.getById(command.memberId());
        TemporalMemo savedTemporalMemo = temporalMemoRepository.save(TemporalMemo.of(member, command.content()));
        return SaveTemporalMemoResult.from(savedTemporalMemo);
    }

    public void updateTemporalMemo(UpdateTemporalMemoCommand command) {
        TemporalMemo temporalMemo = temporalMemoRepository.getById(command.temporalMemoId());
        temporalMemo.matchOwner(command.memberId());
        temporalMemo.updateContent(MemoContent.from(command.content()));
    }

    public void deleteTemporalMemo(DeleteTemporalMemoCommand command) {
        TemporalMemo temporalMemo = temporalMemoRepository.getById(command.temporalMemoId());
        temporalMemo.matchOwner(command.memberId());
        temporalMemoRepository.delete(temporalMemo);
    }

    public void applyCorrection(ApplyCorrectionCommand command) {
        TemporalMemo temporalMemo = temporalMemoRepository.getById(command.temporalMemoId());
        temporalMemo.matchOwner(command.memberId());
        temporalMemo.applyCorrection(MemoContent.from(command.contents()));
    }

    @Transactional(readOnly = true)
    public List<FindTemporalMemoHistoriesResult> findTemporalMemos(FindTemporalMemoHistoriesQuery query) {
        validateQueryDateRange(query);
        List<TemporalMemo> temporalMemos = temporalMemoRepository.findAllByMemberIdAndCreatedAtBetween(query.memberId(),
                query.startDate().atStartOfDay(), query.endDate().atTime(LocalTime.MAX));
        Map<LocalDate, List<TemporalMemo>> temporalMemosByDate = groupTemporalMemosByCreatedAt(temporalMemos);

        return temporalMemosByDate.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .map(createdAt -> new FindTemporalMemoHistoriesResult(
                        createdAt, toTemporalMemoResults(temporalMemosByDate.get(createdAt)
                )))
                .toList();
    }

    private void validateQueryDateRange(FindTemporalMemoHistoriesQuery query) {
        if (query.startDate().isAfter(query.endDate())) {
            throw new TemporalMemoException(TemporalMemoExceptionType.NON_SEQUENTIAL_DATES_EXCEPTION);
        }
    }

    private Map<LocalDate, List<TemporalMemo>> groupTemporalMemosByCreatedAt(List<TemporalMemo> temporalMemos) {
        return temporalMemos.stream().collect(Collectors.groupingBy(
                temporalMemo -> temporalMemo.getCreatedAt().toLocalDate()
        ));
    }

    private List<FindTemporalMemoResult> toTemporalMemoResults(List<TemporalMemo> temporalMemos) {
        return temporalMemos.stream()
                .map(FindTemporalMemoResult::from)
                .sorted(Comparator.comparing(FindTemporalMemoResult::createdAt))
                .toList();
    }
}
