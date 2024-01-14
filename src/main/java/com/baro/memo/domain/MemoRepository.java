package com.baro.memo.domain;

import java.util.List;

public interface MemoRepository {

    Memo save(Memo temporalMemo);

    List<Memo> findAll();

    Memo getById(Long id);
}
