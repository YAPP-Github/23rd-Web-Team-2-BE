package com.baro.memo.domain;

import com.baro.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;

public interface TemporalMemoRepository {

    TemporalMemo save(TemporalMemo temporalMemo);

    List<TemporalMemo> findAll();

    TemporalMemo getById(Long id);

    void delete(TemporalMemo temporalMemo);

    List<TemporalMemo> findAllByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start, LocalDateTime end);

    void deleteAllByCreatedAtLessThanEqual(LocalDateTime weekBefore);

    void deleteAllByMember(Member member);

    TemporalMemo getByArchiveId(Long archiveId);
}
