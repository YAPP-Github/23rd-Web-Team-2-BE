package com.baro.memo.domain;

import java.util.List;

public interface TemporalMemoRepository {

    TemporalMemo save(TemporalMemo temporalMemo);

    List<TemporalMemo> findAll();
}
